package com.introid.introid_food_app.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.introid.introid_food_app.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_app_bar.setupWithNavController(foodItemsFragment.findNavController())

        foodItemsFragment.findNavController()
            .addOnDestinationChangedListener{_ , destination , _ ->
                when(destination.id){
                    R.id.homeFragment, R.id.ordersFragment ->
                        bottom_app_bar.visibility = View.VISIBLE
                    else -> bottom_app_bar.visibility = View.GONE
                }
            }

    }
}