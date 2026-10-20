package com.example.foodappmvi.data.repository

import com.example.foodappmvi.data.database.FoodDao
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val dao : FoodDao
) {

    fun getAllFoods() = dao.getAllFoods()

}