package com.example.foodappmvi.view.home

import com.example.foodappmvi.data.model.remote.ResponseCategoriesList
import com.example.foodappmvi.data.model.remote.ResponseFoodList

sealed class HomeState {

    data object Idle : HomeState()
    data class ShowFilterLettersAZ(val lettersAZ: MutableList<Char>) : HomeState()
    data class ShowRandomFood(val food: ResponseFoodList.Meal?) : HomeState()
    data class ShowError(val errorMessage: String) : HomeState()
    data object ShowLoadingCategory : HomeState()
    data object ShowLoadingFoods : HomeState()
    data class ShowCategoriesList(val categoriesList: MutableList<ResponseCategoriesList.Category>) : HomeState()
    data class ShowFoodsList(val foodsList: MutableList<ResponseFoodList.Meal>) : HomeState()
    data object ShowEmptySection : HomeState()

}