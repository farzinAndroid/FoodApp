package com.example.foodappmvp.data.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodappmvp.utils.FOOD_DB_TABLE

@Entity(tableName = FOOD_DB_TABLE)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var image: String =""
)