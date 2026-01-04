package com.example.foodappmvp.data.repository

import com.example.foodappmvp.server.ApiServices
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val api: ApiServices
) {

    fun getFoodDetails(id: Int) = api.getFoodDetails(id)

}