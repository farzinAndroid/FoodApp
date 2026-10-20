package com.example.foodappmvi.view.detail

import com.example.foodappmvi.data.model.remote.ResponseFoodList

sealed class DetailState {
    data object ShowLoading : DetailState()
    data object ClosePage : DetailState()
    data class ShowError(val error: String) : DetailState()
    data class ShowFoodDetail(val food: ResponseFoodList) : DetailState()
    data class DeleteFavorite(val deleteFavorite: Unit) : DetailState()
    data class SaveFavorite(val saveFavorite: Unit) : DetailState()
    data class SetFavoriteColor(val isFoodInFavorite: Boolean) : DetailState()
}