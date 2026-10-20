package com.example.foodappmvi.view.detail

import com.example.foodappmvi.data.model.database.FoodEntity

sealed class DetailIntent {

    data object ClosePage : DetailIntent()
    data class GetFoodDetail(val id: Int) : DetailIntent()
    data class IsFoodExists(val id: Int) : DetailIntent()
    data class SaveFoodToFavorites(val foodEntity: FoodEntity) : DetailIntent()
    data class DeleteFoodFromFavorite(val foodEntity: FoodEntity) : DetailIntent()

}