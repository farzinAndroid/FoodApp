package com.example.foodappmvp.data.repository

import com.example.foodappmvp.data.model.database.FoodEntity
import com.example.foodappmvp.database.FoodDao
import com.example.foodappmvp.server.ApiServices
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val dao: FoodDao
) {

    fun getAllFoods() = dao.getAllFoods()

}