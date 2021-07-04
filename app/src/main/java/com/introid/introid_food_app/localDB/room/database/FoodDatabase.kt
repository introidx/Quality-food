package com.introid.introid_food_app.localDB.room.database

import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.introid.introid_food_app.models.Food
import com.introid.introid_food_app.util.Constants.TAG
import kotlinx.coroutines.tasks.await

object FoodDatabase {
    private val firestore = FirebaseFirestore.getInstance()
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val user: FirebaseUser? = auth.currentUser

    private val recommendedList = firestore.collection("recommended")

    suspend fun getRecommendedListItem(): List<Food> {
        return try {
            recommendedList.get().await().toObjects(Food::class.java)
        } catch (e: Exception) {
            emptyList()
        }

    }
}

/*    suspend fun saveUserInfo(info: String): Any {
        return try {
            if (user != null) {
                firestore.collection("users/${user.uid}").add(info).await().toString()
            } else {

            }
        } catch (e: Exception) {

        }

    }*/


/*
private fun saveVisitingCardToFirebase(agent: Agent) {
    firebaseDb.collection("users/${user!!.uid}/agentInfo")
        .add(agent)
        .addOnSuccessListener {
            Toast.makeText(this@CreateVisitingCard, " Saved Visiting Card Details to firebase !!", Toast.LENGTH_SHORT).show()
            createFolderOfWalletToFirebase()
        }
        .addOnFailureListener {
            Toast.makeText(this@CreateVisitingCard, "Saving Visiting Card Failed $it", Toast.LENGTH_SHORT).show()
            binding.evcfBtnSubmit.visibility = View.VISIBLE
            binding.visitingCardProgress.visibility = View.GONE
        }
}*/
