package com.example.recipeappmvvm.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeappmvvm.data.model.database.FoodEntity

@Database(entities = [FoodEntity::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun getFoodDao() : FoodDao

}