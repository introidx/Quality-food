package com.introid.introid_food_app.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "cart_table")
data class Cart (
        @PrimaryKey(autoGenerate = true)
        val id : Int = -1,
        val foodName : String = "",
        val imageUrl : String = "",
        var qty : Int = -1,
        var priceOfOne : Int = -1,
        var individualTotalPrice : Int = -1
) : Parcelable
