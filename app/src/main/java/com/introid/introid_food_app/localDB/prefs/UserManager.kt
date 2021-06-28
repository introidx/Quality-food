package com.introid.introid_food_app.localDB.prefs

import android.content.Context
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context : Context) {

    //Create the dataStore
    private val dataStore = context.createDataStore(name = "user_prefs")

    //create keys
    companion object{
        val USER_NAME_KEY = preferencesKey<String>("USER_NAME")
        val USER_EMAIL_KEY = preferencesKey<String>("USER_EMAIL")
        val USER_PHONE_KEY = preferencesKey<String>("USER_PHONE")
    }

    //store user data
    suspend fun storeUser(name : String , email : String , phone : String){
        dataStore.edit {
            it[USER_NAME_KEY] = name
            it[USER_EMAIL_KEY] = email
            it[USER_PHONE_KEY] = phone

        }
    }

    // get data
    val userNameFlow : Flow<String> = dataStore.data.map {
        it[USER_NAME_KEY] ?: ""
    }

    val userEmailFlow : Flow<String> = dataStore.data.map {
        it[USER_EMAIL_KEY] ?: ""
    }

    val userPhoneFlow : Flow<String> = dataStore.data.map {
        it[USER_PHONE_KEY] ?: ""
    }
}