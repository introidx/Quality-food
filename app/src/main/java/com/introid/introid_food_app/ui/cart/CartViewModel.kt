package com.introid.introid_food_app.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.introid.introid_food_app.localDB.room.database.CartDatabase
import com.introid.introid_food_app.localDB.room.repository.CartRepository
import com.introid.introid_food_app.models.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : CartRepository
    val getAllCartItems: LiveData<List<Cart>>

    init{
        val cartDao = CartDatabase.getDatabase(application).cartDao()
        repository = CartRepository(cartDao)
        getAllCartItems = repository.getAllCartItems
    }

    fun addItemToCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItemToCart(cart)
        }
    }

    fun updateItemToCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCartItem(cart)
        }
    }

    fun deleteItemFromCart(cart: Cart){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItemFromCart(cart)
        }
    }

    fun deleteAllItemsFromCart(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllCartItems
        }
    }

}