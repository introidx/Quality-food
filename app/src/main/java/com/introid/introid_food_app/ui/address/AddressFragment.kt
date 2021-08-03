package com.introid.introid_food_app.ui.address

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.asLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.introid.introid_food_app.R
import com.introid.introid_food_app.localDB.prefs.AddressManager
import com.introid.introid_food_app.util.Constants.TAG
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddressFragment : Fragment(R.layout.fragment_address), OnMapReadyCallback {


    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }


    private lateinit var addressManager: AddressManager

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null


    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressManager = AddressManager(requireContext())
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        getLocationPermission()

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // get address data from data store
        putDataOnMap()
        // store address data to data store
        storeAddressDataToDataStore()


    }


    private fun storeAddressDataToDataStore() {
        btn_confirm_address.setOnClickListener {
            val address = et_address_from_map.text.toString()
            if (address.isNotEmpty()) {
                GlobalScope.launch {
                    addressManager.storeAddress(address)
                }
            }
        }
    }

    private fun putDataOnMap() {
        addressManager.userAddressFlow.asLiveData().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                et_address_from_map.setText(it)
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        Log.d(TAG, "onMapReady: Map Loads")
//        getDeviceLocation()


        map?.addMarker(
            MarkerOptions().position(defaultLocation).title("Sydney")
        )
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }


    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        if (locationPermissionGranted) {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        val location =
                            LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                        Log.d(TAG, "getDeviceLocation: Location = $location")
                        map?.addMarker(
                            MarkerOptions().position(location).title("Empty Title")
                        )

                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude
                                ), 10f
                            )
                        )
                    }
                } else {
                    Log.d(TAG, "Current location is null. Using defaults.")
                    Log.e(TAG, "Exception: %s", task.exception)
                    map?.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, 10f)
                    )
                    map?.uiSettings?.isMyLocationButtonEnabled = false
                }
            }

        }
    }


/*    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_LOCATION_REQUEST_CODE
            )
        }
    }*/

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
            locationPermissionGranted = true

            Log.d(
                TAG,
                "getLocationPermission: LocationPermissionGranted = $locationPermissionGranted"
            )
        } else {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_LOCATION_REQUEST_CODE
                )
            }
        }
    }
    //update

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                map?.isMyLocationEnabled = true

            } else {
                Toast.makeText(requireContext(), "Permission Not Granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}
