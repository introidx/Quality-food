package com.introid.introid_food_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.introid.introid_food_app.R
import com.introid.introid_food_app.models.Services
import com.introid.introid_food_app.ui.home.HomeFragment
import kotlinx.android.synthetic.main.item_ingredients.view.*
import kotlinx.android.synthetic.main.our_services_items.view.*

class ServiceAdapter(
        val context: HomeFragment, private val serviceList: List<Services>
) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>(){

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.our_services_items,parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = serviceList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentItem.img)
                    .placeholder(R.drawable.ic_drink)
                    .into(iv_services)
            tv_services.text = currentItem.name
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }


}