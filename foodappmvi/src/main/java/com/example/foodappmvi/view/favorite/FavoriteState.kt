package com.example.foodappmvi.view.favorite

import com.example.foodappmvi.data.model.database.FoodEntity

sealed class FavoriteState {

    data object ShowEmpty : FavoriteState()
    data class ShowFavoritesList(val favorites : MutableList<FoodEntity>) : FavoriteState()


}