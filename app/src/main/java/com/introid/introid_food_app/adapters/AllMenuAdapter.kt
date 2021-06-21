package com.introid.introid_food_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.introid.introid_food_app.R
import com.introid.introid_food_app.models.Food
import com.introid.introid_food_app.ui.home.HomeFragment
import kotlinx.android.synthetic.main.all_items_rv.view.*

class AllMenuAdapter(
        val context: HomeFragment, private val allItemsList: List<Food>
) : RecyclerView.Adapter<AllMenuAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.all_items_rv,parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem = allItemsList[position]
        holder.itemView.apply {
            Glide.with(this)
                    .load(currentItem.imageUrl)
                    .placeholder(R.drawable.recommended1)
                    .into(iv_product)
            all_menu_name.text = currentItem.foodName
            all_menu_note.text = currentItem.note
            product_price.text = "₹ " + currentItem.price.toString()
            setOnClickListener {
                onItemClickListener?.let {
                    it(currentItem)
                }
            }
        }
    }

    override fun getItemCount(): Int {
       return allItemsList.size
    }

    private var onItemClickListener: ((Food) -> Unit)? = null

    fun setOnItemClickListener(listener: (Food) -> Unit) {
        onItemClickListener = listener
    }
}