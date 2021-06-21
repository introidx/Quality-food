package com.introid.introid_food_app.models

import java.io.Serializable

class Food(
        val id : Int = -1,
        val foodName : String ="",
        val note : String ="",
        val imageUrl : String = "",
        val price : Int = -1
) : Serializable
