package com.example.recipeappmvp.data.repository

import androidx.activity.result.contract.ActivityResultContracts
import com.example.recipeappmvp.server.ApiServices
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val api: ApiServices
) {

    fun getFoodDetails(id: Int) = api.getFoodDetails(id)

}