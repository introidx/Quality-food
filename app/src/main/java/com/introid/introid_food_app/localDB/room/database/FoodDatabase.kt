package com.introid.introid_food_app.localDB.room.database

import com.google.firebase.firestore.FirebaseFirestore
import com.introid.introid_food_app.models.Food
import kotlinx.coroutines.tasks.await

object FoodDatabase {
    private val firestore = FirebaseFirestore.getInstance()
    private val recommendedList = firestore.collection("recommended")

    suspend fun getRecommendedListItem() : List<Food>{
        return try {
            recommendedList.get().await().toObjects(Food::class.java)
        }catch (e :Exception){
            emptyList()
        }

    }
}