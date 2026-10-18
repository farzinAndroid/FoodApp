package com.example.foodappmvi.data.repository

import com.example.foodappmvi.data.remote.ApiServices
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: ApiServices) {

    suspend fun getRandomFood() = api.getRandomFood()
    suspend fun getCategoriesFoodList() = api.getCategoriesFoodList()
    suspend fun getFoodListByLetter(letter: String) = api.getFoodListByLetter(letter)
    suspend fun searchFoodList(letter: String) = api.searchFoodList(letter)
    suspend fun getFoodsByCategory(letter: String) = api.getFoodsByCategory(letter)


}