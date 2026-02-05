package com.example.foodappmvp.data.repository

import com.example.foodappmvp.data.model.database.FoodEntity
import com.example.foodappmvp.database.FoodDao
import com.example.foodappmvp.server.ApiServices
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val api: ApiServices,
    private val dao: FoodDao
) {

    fun getFoodDetails(id: Int) = api.getFoodDetails(id)

    fun saveFood(food: FoodEntity) = dao.saveFood(food)
    fun deleteFood(food: FoodEntity) = dao.deleteFood(food)
    fun isFoodExist(id: Int) = dao.isFoodExist(id)

}