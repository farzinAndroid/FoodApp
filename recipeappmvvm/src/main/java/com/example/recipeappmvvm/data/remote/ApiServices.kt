package com.example.recipeappmvvm.data.remote

import com.example.recipeappmvvm.data.model.remote.ResponseCategoriesList
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("random.php")
    suspend fun getRandomFood(): Response<ResponseFoodList>

    @GET("categories.php")
    suspend fun getCategoriesFoodList():Response<ResponseCategoriesList>

    @GET("search.php")
    suspend fun getFoodListByLetter(@Query("f") letter: String): Response<ResponseFoodList>

    @GET("search.php")
    suspend fun searchFoodList(@Query("s") letter: String): Response<ResponseFoodList>

    @GET("filter.php")
    suspend fun getFoodsByCategory(@Query("c") letter: String): Response<ResponseFoodList>

    @GET("lookup.php")
    suspend fun getFoodDetails(@Query("i") id: Int): Response<ResponseFoodList>
}
