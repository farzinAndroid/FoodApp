package com.example.recipeappmvvm.data.repository

import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.data.model.remote.ResponseCategoriesList
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import com.example.recipeappmvvm.data.remote.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: ApiServices) {

    fun getRandomFood(): Flow<Response<ResponseFoodList>> {
        return flow {
            emit(api.getRandomFood())
        }.flowOn(Dispatchers.IO)
    }


    fun getCategoriesFoodList(): Flow<MyResponse<ResponseCategoriesList>> {
        return flow {
            emit(MyResponse.loading())

            when (api.getCategoriesFoodList().code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.getCategoriesFoodList().body()))
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .catch { emit(MyResponse.error(it.message.toString())) }
    }

    fun getFoodListByLetter(letter: String): Flow<MyResponse<ResponseFoodList>> {
        return flow {
            emit(MyResponse.loading())

            when (api.getFoodListByLetter(letter).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.getFoodListByLetter(letter).body()))
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .catch { emit(MyResponse.error(it.message.toString())) }
    }


    fun searchFoods(search: String): Flow<MyResponse<ResponseFoodList>> {
        return flow {
            emit(MyResponse.loading())

            when (api.searchFoodList(search).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.searchFoodList(search).body()))
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .catch { emit(MyResponse.error(it.message.toString())) }
    }

    fun getFoodListByCategory(category: String): Flow<MyResponse<ResponseFoodList>> {
        return flow {
            emit(MyResponse.loading())

            when (api.getFoodsByCategory(category).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.getFoodsByCategory(category).body()))
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .catch { emit(MyResponse.error(it.message.toString())) }
    }

}