package com.example.foodappmvp.ui.favorite

import com.example.foodappmvp.base.BasePresenter
import com.example.foodappmvp.data.model.database.FoodEntity

interface FavoriteContracts {

    interface View{
        fun showEmptyList()
        fun showFavoriteList(list: MutableList<FoodEntity>)
    }

    interface Presenter : BasePresenter{
        fun getAllFavoriteFoods()
    }

}