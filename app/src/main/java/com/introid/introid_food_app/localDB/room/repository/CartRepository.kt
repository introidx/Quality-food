package com.introid.introid_food_app.localDB.room.repository

import androidx.lifecycle.LiveData
import com.introid.introid_food_app.localDB.room.dao.CartDao
import com.introid.introid_food_app.models.Cart

class CartRepository(private val cartDao: CartDao) {

    suspend fun addItemToCart(cart: Cart){
        cartDao.addItemToCart(cart)
    }

    suspend fun updateCartItem(cart: Cart){
        cartDao.updateItemInCart(cart)
    }

    val getAllCartItems : LiveData<List<Cart>> = cartDao.getAllItemsFromCart()

    suspend fun deleteItemFromCart(cart: Cart){
        cartDao.deleteItemInCart(cart)
    }

    suspend fun deleteAllItemsFromCart(){
        cartDao.deleteAllItemsFromCart()
    }

}