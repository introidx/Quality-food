package com.introid.introid_food_app.localDB.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.introid.introid_food_app.localDB.room.dao.CartDao
import com.introid.introid_food_app.models.Cart

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            val tempInstance: CartDatabase? = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance: CartDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        CartDatabase::class.java,
                        "cart_database",
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }


}