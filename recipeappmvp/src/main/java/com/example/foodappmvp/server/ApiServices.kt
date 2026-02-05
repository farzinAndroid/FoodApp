package com.example.foodappmvp.server

import com.example.foodappmvp.data.model.ResponseCategoriesList
import com.example.foodappmvp.data.model.ResponseFoodList
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("random.php")
    fun getRandomFood(): Single<Response<ResponseFoodList>>

    @GET("categories.php")
    fun getCategoriesFoodList(): Single<Response<ResponseCategoriesList>>

    @GET("search.php")
    fun getFoodListByLetter(@Query("f") letter: String): Single<Response<ResponseFoodList>>

    @GET("search.php")
    fun searchFoodList(@Query("s") letter: String): Single<Response<ResponseFoodList>>

    @GET("filter.php")
    fun getFoodsByCategory(@Query("c") letter: String): Single<Response<ResponseFoodList>>

    @GET("lookup.php")
    fun getFoodDetails(@Query("i") id: Int): Single<Response<ResponseFoodList>>
}
