package com.introid.introid_food_app.localDB.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.introid.introid_food_app.models.Cart

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItemToCart(cart: Cart)

    @Update
    suspend fun updateItemInCart(cart: Cart)

    @Delete
    suspend fun deleteItemInCart(cart: Cart)

    @Query("SELECT * FROM cart_table order by id DESC")
    fun getAllItemsFromCart() : LiveData<List<Cart>>

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllItemsFromCart()

}