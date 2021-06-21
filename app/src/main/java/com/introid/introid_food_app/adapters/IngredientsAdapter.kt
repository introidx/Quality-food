package com.introid.introid_food_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.introid.introid_food_app.R
import com.introid.introid_food_app.models.Ingredients
import com.introid.introid_food_app.ui.productDetail.ProductDetailFragment
import kotlinx.android.synthetic.main.item_ingredients.view.*

class IngredientsAdapter(
    val context: ProductDetailFragment, private val ingredientsList: List<Ingredients>
) : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_ingredients,parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = ingredientsList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentItem.img)
                    .placeholder(R.drawable.popular1)
                    .into(iv_ingredient)
        }
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }
}