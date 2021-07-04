package com.introid.introid_food_app.models

data class Order(
    val cartList: List<Cart>,
    val userAddress: Address
)