package com.example.recipeappmvvm.data.repository

import com.example.recipeappmvvm.data.database.FoodDao
import com.example.recipeappmvvm.data.model.database.FoodEntity
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val dao: FoodDao) {


    suspend fun saveFood(foodEntity: FoodEntity) = dao.saveFood(foodEntity)
    suspend fun deleteFood(foodEntity: FoodEntity) = dao.deleteFood(foodEntity)
    fun getAllFoods() = dao.getAllFoods()
    fun isFoodExist(id: Int) = dao.isFoodExist(id)

}