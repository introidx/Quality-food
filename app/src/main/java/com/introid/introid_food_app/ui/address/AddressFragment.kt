package com.introid.introid_food_app.ui.address

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.introid.introid_food_app.R
import com.introid.introid_food_app.models.Address
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.android.synthetic.main.fragment_address.*
import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.asLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.introid.introid_food_app.localDB.prefs.AddressManager
import com.introid.introid_food_app.util.Constants.TAG
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddressFragment : Fragment(R.layout.fragment_address),OnMapReadyCallback ,  EasyPermissions.PermissionCallbacks  {


    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private var map: GoogleMap? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var addressManager: AddressManager

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: Called")
        addressManager = AddressManager(requireContext())



/*        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (hasLocationPermission()) {
           fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
//               Log.d(TAG, "onViewCreated: $location.latitude, $location.longitude")
//               val latLng = LatLng(location.latitude, location.longitude)
//               map?.let {
//                   it.addMarker(MarkerOptions().position(latLng))
//                   it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
//               }
           }
        } else {
            requestLocationPermission()
        }*/

        mapView.getMapAsync{
            map = it
            Log.d(TAG, "onViewCreated: Map == $map")
        }

        // get address data from data store
        putDataOnMap()
        // store address data to data store
        storeAddressDataToDataStore()


    }

    private fun storeAddressDataToDataStore() {
        btn_confirm_address.setOnClickListener {
            val address = et_address_from_map.text.toString()
            if (address.isNotEmpty()){
                GlobalScope.launch {
                    addressManager.storeAddress(address)
                }
            }
        }
    }

    private fun putDataOnMap() {
        addressManager.userAddressFlow.asLiveData().observe(viewLifecycleOwner , {
            if (it.isNotEmpty()){
                et_address_from_map.setText(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_address , container, false)
    }

    override fun onMapReady(p0: GoogleMap?) {
        Log.d(TAG, "onMapReady: this is being called")
        val latLng = LatLng(25.096073, 85.313118)
        map?.let {
            it.addMarker(MarkerOptions().position(latLng))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
        }

    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application cannot work without Location Permission.",
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
    }


}