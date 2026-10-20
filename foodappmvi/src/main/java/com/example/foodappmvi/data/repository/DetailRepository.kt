package com.example.foodappmvi.data.repository

import com.example.foodappmvi.data.database.FoodDao
import com.example.foodappmvi.data.model.database.FoodEntity
import com.example.foodappmvi.data.remote.ApiServices
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val api: ApiServices,
    private val dao: FoodDao
) {

    suspend fun getFoodDetails(id: Int) = api.getFoodDetails(id)
    suspend fun saveFood(foodEntity: FoodEntity) = dao.saveFood(foodEntity)
    suspend fun deleteFood(foodEntity: FoodEntity) = dao.deleteFood(foodEntity)
    suspend fun isFoodExist(id: Int) = dao.isFoodExist(id)


}