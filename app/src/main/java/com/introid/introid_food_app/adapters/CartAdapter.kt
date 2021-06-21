package com.introid.introid_food_app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.introid.introid_food_app.R
import com.introid.introid_food_app.models.Cart
import com.introid.introid_food_app.ui.cart.CheckoutPageFragment
import com.introid.introid_food_app.util.Constants.TAG
import kotlinx.android.synthetic.main.cart_item_new_design.view.*
import kotlinx.android.synthetic.main.cart_items.view.tv_qty


class CartAdapter(
    private val changeItemsInCart: ChangeItemsInCart,
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var cartList = emptyList<Cart>()

    interface ChangeItemsInCart {
        fun plusButtonClick(position: Int)
        fun minusButtonClick(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cart_item_new_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: on Bind View Holder")
        val currentItem = cartList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentItem.imageUrl)
                .placeholder(R.drawable.popular1)
                .into(iv_cart)

            tv_cart.text = currentItem.foodName
            tv_qty.text = currentItem.qty.toString()
            tv_cart_price.text = currentItem.individualTotalPrice.toString()

            tv_plus.setOnClickListener {
                changeItemsInCart.plusButtonClick(position)
            }

            tv_minus.setOnClickListener {
                changeItemsInCart.minusButtonClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    fun setData(cart: List<Cart>) {
        this.cartList = cart
        notifyDataSetChanged()
    }
}