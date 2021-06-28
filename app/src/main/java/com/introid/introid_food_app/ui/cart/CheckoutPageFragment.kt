package com.introid.introid_food_app.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.introid.introid_food_app.R
import com.introid.introid_food_app.adapters.CartAdapter
import com.introid.introid_food_app.localDB.prefs.AddressManager
import com.introid.introid_food_app.localDB.prefs.UserManager
import com.introid.introid_food_app.models.Cart
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

    // data store
    private lateinit var userManager: UserManager
    private lateinit var addressManager: AddressManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        // data store
        userManager = UserManager(requireContext())
        addressManager = AddressManager(requireContext())

        getUserDataFromDataStore()
//        if (args != null){
//            val address_data = args.address
//            Toast.makeText(context, address_data.userName, Toast.LENGTH_SHORT).show()
//        }


        cartAdapter = CartAdapter(object : CartAdapter.ChangeItemsInCart {
            override fun plusButtonClick(position: Int) {
                addQuantityAndChangeTotalPrice(position)
            }

            override fun minusButtonClick(position: Int) {
                reduceQuantityAndChangeTotalPrice(position)
            }
        })

        getFoodDataFromDb()
        rv_cart.adapter = cartAdapter
        cartAdapter.notifyDataSetChanged()

        tv_change_location.setOnClickListener {
            findNavController().navigate(R.id.action_checkoutPageFragment_to_addressFragment)
        }

        // make payment
        cv_make_payment.setOnClickListener {
            makePayment()
        }
    }

    private fun getUserDataFromDataStore() {
        userManager.userNameFlow.asLiveData().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                tv_deliver_to.text = "Deliver to : $it"
            }
        })

        addressManager.userAddressFlow.asLiveData().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                tv_location_to_deliver.text = it
            }
        })
    }

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

    private fun getFoodDataFromDb() {
        cartViewModel.getAllCartItems.observe(viewLifecycleOwner, Observer {
            cartAdapter.setData(it)
            cartList = it
            cartAdapter.notifyDataSetChanged()
            var totalPrice =0
            for(items in it){
                totalPrice += items.individualTotalPrice
            }
            tv_amount.text = "₹ $totalPrice"

        })
    }

    override fun onPaymentSuccess(p0: String?) {
        // upload cart data to fire store
    }

    // since we are using test payment method from RazorPay it will always give error message
    // so upload cart data to fire store in this case also.
    override fun onPaymentError(p0: Int, p1: String?) {
        // upload cart data to fire store
    }




}