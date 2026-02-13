package com.example.recipeappmvvm.data.repository

import com.example.recipeappmvvm.data.database.FoodDao
import com.example.recipeappmvvm.data.model.database.FoodEntity
import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import com.example.recipeappmvvm.data.remote.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val api: ApiServices,
    private val dao: FoodDao
) {

    suspend fun getFoodDetail(foodId: Int) : Flow<MyResponse<ResponseFoodList>> {
        return flow {
            emit(MyResponse.loading())

            when (api.getFoodDetails(foodId).code()) {
                in 200..202 -> {
                    emit(MyResponse.success(api.getFoodDetails(foodId).body()))
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .catch { emit(MyResponse.error(it.message.toString())) }
    }



    suspend fun saveFood(foodEntity: FoodEntity) = dao.saveFood(foodEntity)
    suspend fun deleteFood(foodEntity: FoodEntity) = dao.deleteFood(foodEntity)

    fun isFoodExist(id: Int) = dao.isFoodExist(id)

}