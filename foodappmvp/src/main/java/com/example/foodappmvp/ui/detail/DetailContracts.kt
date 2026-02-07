package com.example.foodappmvp.ui.detail

import com.example.foodappmvp.base.BasePresenter
import com.example.foodappmvp.base.BaseView
import com.example.foodappmvp.data.model.ResponseFoodList
import com.example.foodappmvp.data.model.database.FoodEntity

interface DetailContracts {

    interface View : BaseView{
        fun showDetail(response: ResponseFoodList)
        fun updateFavorite(isFavorite: Boolean)
    }


    interface Presenter : BasePresenter{
        fun getFoodDetails(id:Int)
        fun saveFood(foodEntity: FoodEntity)
        fun deleteFood(foodEntity: FoodEntity)
        fun isFoodExists(foodId: Int)
    }

}