package com.example.foodappmvi.view.favorite

sealed class FavoriteIntent {

    data object LoadFavorites : FavoriteIntent()


}