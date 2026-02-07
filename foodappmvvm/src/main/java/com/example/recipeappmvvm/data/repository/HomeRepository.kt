package com.example.recipeappmvvm.data.repository

import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.data.model.remote.ResponseCategoriesList
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import com.example.recipeappmvvm.data.remote.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: ApiServices) {

    suspend fun getRandomFood() : Flow<Response<ResponseFoodList>>{
        return flow {
            emit(api.getRandomFood())
        }.flowOn(Dispatchers.IO)
    }


    suspend fun getCategoriesFoodList() : Flow<MyResponse<ResponseCategoriesList>> {
        return flow {
            emit(MyResponse.loading())

            when(api.getCategoriesFoodList().code()){
                in 200..202->{
                    emit(MyResponse.success(api.getCategoriesFoodList().body()))
                }
                422->{
                    emit(MyResponse.error("Error"))
                }
                in 400..499->{
                    emit(MyResponse.error("Error"))
                }
                in 500..599->{
                    emit(MyResponse.error("Error"))
                }
            }
        }
    }

}