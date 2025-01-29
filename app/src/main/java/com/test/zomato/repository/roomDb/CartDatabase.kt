package com.test.zomato.repository.roomDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.zomato.view.location.models.UserSavedAddress
import com.test.zomato.view.location.repository.UserSavedAddressDao
import com.test.zomato.view.login.repository.UserDao
import com.test.zomato.view.login.userData.User
import com.test.zomato.view.main.home.models.FoodItem
import com.test.zomato.view.main.home.models.orderModels.FoodItemInOrder
import com.test.zomato.view.main.home.models.orderModels.OrderDetails

@Database(entities =[User::class,FoodItem::class, OrderDetails::class, FoodItemInOrder::class,UserSavedAddress::class], version = 1, exportSchema = false)
abstract class CartDatabase:RoomDatabase() {

    abstract fun cartDao(): CartDao

    abstract fun userDao(): UserDao

    abstract fun userSavedAddressDao(): UserSavedAddressDao

    companion object {
        private var instance: CartDatabase? = null
        fun getInstance( context: Context): CartDatabase? {
            if (instance == null) {
                synchronized(CartDatabase::class.java) {
                    instance = Room.databaseBuilder(context.applicationContext, CartDatabase::class.java, "zosadedswmat")
                        .build()
                }
            }
            return instance
        }
    }


}