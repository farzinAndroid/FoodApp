package com.example.foodappmvi.view.home

sealed class HomeIntent {

    data object CreateFilterLetters: HomeIntent()
    data object LoadRandomFoods: HomeIntent()
    data object LoadCategoriesList: HomeIntent()
    data class LoadFoodsByLetterList(val letter: String): HomeIntent()
    data class LoadSearchFoods(val search: String): HomeIntent()
    data class LoadFoodsByCategory(val category: String): HomeIntent()

}