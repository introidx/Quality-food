package com.introid.introid_food_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.introid.introid_food_app.R
import com.introid.introid_food_app.models.Cart
import com.introid.introid_food_app.models.Food
import com.introid.introid_food_app.ui.home.HomeFragment
import kotlinx.android.synthetic.main.recomended_recycler_view_items.view.*
import kotlinx.android.synthetic.main.recomended_recycler_view_items.view.recommended_image
import kotlinx.android.synthetic.main.recommended_items_new.view.*

class RecommendedAdapter : RecyclerView.Adapter<RecommendedAdapter.ViewHolder>() {

    private var recommendedList = emptyList<Food>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recommended_items_new, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = recommendedList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(currentItem.imageUrl)
                .placeholder(R.drawable.recommended1)
                .into(iv_recommended)
            tv_recommended.text = currentItem.foodName
            tv_price_rec.text = currentItem.price.toString()
            setOnClickListener {
                onItemClickListener?.let {
                    it(currentItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return recommendedList.size
    }

    private var onItemClickListener: ((Food) -> Unit)? = null

    fun setOnItemClickListener(listener: (Food) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(cart: List<Food>) {
        this.recommendedList = cart
        notifyDataSetChanged()
    }

}