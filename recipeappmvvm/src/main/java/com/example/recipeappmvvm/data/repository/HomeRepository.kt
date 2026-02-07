package com.example.recipeappmvvm.data.repository

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

}