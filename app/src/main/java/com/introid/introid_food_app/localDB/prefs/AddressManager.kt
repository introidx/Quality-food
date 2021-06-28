package com.introid.introid_food_app.localDB.prefs

import android.content.Context
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddressManager(context: Context) {

    //create dataStore
    private val dataStore = context.createDataStore("user_address_prefs")

    //create keys
    companion object{
        val USER_ADDRESS_KEY = preferencesKey<String>("USER_ADDRESS")
    }

    // store address
    suspend fun storeAddress(address : String){
        dataStore.edit {
            it[USER_ADDRESS_KEY] = address
        }
    }

    // get address
    val userAddressFlow : Flow<String> = dataStore.data.map {
        it[USER_ADDRESS_KEY] ?: ""
    }
}