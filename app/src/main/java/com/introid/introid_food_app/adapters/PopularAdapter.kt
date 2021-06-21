package com.introid.introid_food_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.introid.introid_food_app.R
import com.introid.introid_food_app.models.Food
import com.introid.introid_food_app.ui.home.HomeFragment
import kotlinx.android.synthetic.main.popular_recycler_view_items.view.*

public class PopularAdapter(
    val context: HomeFragment, private val popularList: List<Food>
) : RecyclerView.Adapter<PopularAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.popular_recycler_view_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = popularList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentItem.imageUrl)
                    .placeholder(R.drawable.popular1)
                    .into(popular_image)

            popular_name.text = currentItem.foodName
            setOnClickListener {
                onItemClickListener?.let {
                    it(currentItem)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return popularList.size
    }

    private var onItemClickListener: ((Food) -> Unit)? = null

    fun setOnItemClickListener(listener: (Food) -> Unit) {
        onItemClickListener = listener
    }
}