package com.introid.introid_food_app.ui.productDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.introid.introid_food_app.R
import com.introid.introid_food_app.adapters.IngredientsAdapter
import com.introid.introid_food_app.adapters.SimilarProductAdapter
import com.introid.introid_food_app.models.Cart
import com.introid.introid_food_app.models.Food
import com.introid.introid_food_app.models.Ingredients
import com.introid.introid_food_app.ui.cart.CartViewModel
import com.introid.introid_food_app.util.Constants.TAG
import kotlinx.android.synthetic.main.fragment_product_detail.*

class ProductDetailFragment : Fragment(R.layout.fragment_product_detail) {

    var qty : Int = 1
    var individualTotalPrice : Int = 0

    private val args : ProductDetailFragmentArgs by navArgs()
    private val cartCollectionRef = Firebase.firestore.collection("cart")
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var firebaseDbRecommended : FirebaseFirestore
    private lateinit var similarProductList : MutableList<Food>
    private lateinit var similarProductAdapter: SimilarProductAdapter
    private lateinit var cartViewModel: CartViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        similarProductList = mutableListOf()
        similarProductAdapter = SimilarProductAdapter(this, similarProductList)
        rv_similar.adapter = similarProductAdapter
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        firebaseDbRecommended = FirebaseFirestore.getInstance()

        val recommendedReference = firebaseDbRecommended
                .collection("recommended")

        val foodDetail = args.food
        individualTotalPrice = foodDetail.price

        val ingredientsList = mutableListOf(
                Ingredients(R.drawable.popular1),
                Ingredients(R.drawable.popular1),
                Ingredients(R.drawable.popular1),
                Ingredients(R.drawable.popular1),
                Ingredients(R.drawable.popular1),
                Ingredients(R.drawable.popular1)
        )
        ingredientsAdapter = IngredientsAdapter(this, ingredientsList)
        rv_ingredients.adapter= ingredientsAdapter

        Glide.with(this).load(foodDetail.imageUrl).into(iv_product)
        tv_product_name.text = foodDetail.foodName
        tv_note.text = foodDetail.note

        tv_plus.setOnClickListener {
            qty++
            tv_qty.text = qty.toString()
            individualTotalPrice = qty * foodDetail.price
            food_price.text = "₹ $individualTotalPrice"
        }

        // add item to the cart
        btn_add_to_cart.setOnClickListener{
            val cart = Cart(foodDetail.id , foodDetail.foodName , foodDetail.imageUrl, qty, foodDetail.price, individualTotalPrice )
            addToCart(cart)
        }

        recommendedReference.addSnapshotListener{snapshot, exception ->
            if (exception != null || snapshot == null){
                Log.d(TAG, "onCreate: ", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects(Food::class.java)
            similarProductList.clear()
            similarProductList.addAll(postList)
            similarProductAdapter.notifyDataSetChanged()

        }



        btn_go_to_cart.setOnClickListener {
            findNavController().navigate(
                    R.id.action_productDetailFragment_to_checkoutPageFragment
            )
        }

    }

    private fun addToCart(cart: Cart) {
        cartViewModel.addItemToCart(cart)
        Log.d(TAG, "addToCart: Item Added to cart Success!!")
        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE
        // show go to cart button
    }




}