package com.introid.introid_food_app.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.introid.introid_food_app.R
import com.introid.introid_food_app.adapters.CartAdapter
import com.introid.introid_food_app.localDB.prefs.AddressManager
import com.introid.introid_food_app.localDB.prefs.UserManager
import com.introid.introid_food_app.models.Address
import com.introid.introid_food_app.models.Cart
import com.introid.introid_food_app.models.Order
import com.introid.introid_food_app.util.Constants.TAG
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.fragment_checkout_page.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt


class CheckoutPageFragment : Fragment(R.layout.fragment_checkout_page), PaymentResultListener {

    private lateinit var firebaseDbCart: FirebaseFirestore
    private lateinit var cartList: List<Cart>
    private lateinit var cartAdapter: CartAdapter

    private lateinit var cartViewModel: CartViewModel

    private var userName: String = ""
    private var userPhone: String = ""
    private var userAddress: String = ""

    // data store
    private lateinit var userManager: UserManager
    private lateinit var addressManager: AddressManager
    // firebase
    var user: FirebaseUser? = null
    lateinit var auth: FirebaseAuth


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        // data store
        userManager = UserManager(requireContext())
        addressManager = AddressManager(requireContext())

        // get ref to auth abd current user
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser

        firebaseDbCart = FirebaseFirestore.getInstance()

        getUserDataFromDataStore()

        cartAdapter = CartAdapter(object : CartAdapter.ChangeItemsInCart {
            override fun plusButtonClick(position: Int) {
                addQuantityAndChangeTotalPrice(position)
            }

            override fun minusButtonClick(position: Int) {
                reduceQuantityAndChangeTotalPrice(position)
            }
        })

        // get food from the local db to show in the checkout list
        getFoodDataFromDb()
        // add adapter to the rv
        rv_cart.adapter = cartAdapter
        cartAdapter.notifyDataSetChanged()

        // change address
        tv_change_location.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutPageFragment_to_addressFragment)
        }

        // make payment
        cv_make_payment.setOnClickListener {
            makePayment()
        }
    }

    // get user from shared preferences ot datastore
    private fun getUserDataFromDataStore() {
        userManager.userNameFlow.asLiveData().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                userName = it
                tv_deliver_to.text = "Deliver to : $it"
            }
        })

        userManager.userPhoneFlow.asLiveData().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                userPhone = it
            }
        })

        addressManager.userAddressFlow.asLiveData().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                userAddress = it
                tv_location_to_deliver.text = it
            }
        })
    }

    // make payment
    private fun makePayment() {
        val sampleAmount = 100
        val amount = (sampleAmount.toFloat() * 100).roundToInt()
        Log.d(TAG, "makePayment: Sample amount $sampleAmount")
        val checkout = Checkout().apply {
            setKeyID("rzp_test_bM96CD2TdumyjO")
            setImage(R.drawable.ic_drink)
        }

        try {
            val jsonObject = JSONObject().apply {
                put("name", "Quality Food")
                put("description", "Test Payment")
                put("currency", "INR")
                put("amount", amount)
                put("prefill.contact", "7549966862")
                put("prefill.email", "prakash.r19500@gmail.com")
            }
            checkout.open(activity, jsonObject)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    // on click of minus reduce quantity and change total price
    private fun reduceQuantityAndChangeTotalPrice(position: Int) {
        val oldCartItem = cartList[position]
        val newQty = oldCartItem.qty - 1
        if (newQty > 0) {
            val newIndividualTotalPrice = newQty * oldCartItem.priceOfOne
            val newCartItem = Cart(
                oldCartItem.id,
                oldCartItem.foodName,
                oldCartItem.imageUrl,
                newQty,
                oldCartItem.priceOfOne,
                newIndividualTotalPrice
            )
            cartViewModel.updateItemToCart(newCartItem)
            getFoodDataFromDb()
            cartAdapter.notifyDataSetChanged()
        } else {
            cartViewModel.deleteItemFromCart(oldCartItem)
            getFoodDataFromDb()
            cartAdapter.notifyDataSetChanged()

        }
    }

    // on click of plus increase quantity and change total price
    private fun addQuantityAndChangeTotalPrice(position: Int) {
        val oldCartItem = cartList[position]
        val newQty = oldCartItem.qty + 1
        val newIndividualTotalPrice = newQty * oldCartItem.priceOfOne
        val newCartItem = Cart(
            oldCartItem.id,
            oldCartItem.foodName,
            oldCartItem.imageUrl,
            newQty,
            oldCartItem.priceOfOne,
            newIndividualTotalPrice
        )
        cartViewModel.updateItemToCart(newCartItem)
        getFoodDataFromDb()
        cartAdapter.notifyDataSetChanged()
    }

    // get food from db
    private fun getFoodDataFromDb() {
        cartViewModel.getAllCartItems.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it)
            cartList = it
            cartAdapter.notifyDataSetChanged()
            var totalPrice = 0
            for (items in it) {
                totalPrice += items.individualTotalPrice
            }
            tv_amount.text = "??? $totalPrice"

        })
    }

    override fun onPaymentSuccess(p0: String?) {
        // upload cart data to firestore
        uploadDataForUser()
    }

    // since we are using test payment method from RazorPay it will always give error message
    // so upload cart data to firestore in this case also.
    override fun onPaymentError(p0: Int, p1: String?) {
        // upload cart data to fire store
        uploadDataForUser()
    }

    // upload in users folder
    private fun uploadDataForUser() {
        firebaseDbCart.collection("users/${user?.uid}/order")
            .add(cartList).addOnSuccessListener {
                Toast.makeText(requireContext(), "Added to users db", Toast.LENGTH_SHORT).show()
                uploadDataForRestaurant()
            }
            .addOnFailureListener {

            }
    }

    // upload to restaurants folder
    private fun uploadDataForRestaurant() {
        val userAddress = Address(userName, userPhone, userAddress)
        val order = Order(cartList, userAddress)
        firebaseDbCart.collection("Orders")
            .add(order).addOnSuccessListener {
                Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
            }

    }


}