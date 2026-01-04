package com.example.foodappmvp.ui.home

import com.example.foodappmvp.base.BasePresenter
import com.example.foodappmvp.base.BaseView
import com.example.foodappmvp.data.model.ResponseCategoriesList
import com.example.foodappmvp.data.model.ResponseFoodList

interface HomeContracts {
    interface View : BaseView {
        fun showRandomFood(data: ResponseFoodList)
        fun showCategoriesList(categoriesList: ResponseCategoriesList)
        fun showFoodsList(foodsList: ResponseFoodList)
        fun showEmptyList()

    }

    interface Presenter : BasePresenter {
        fun getRandomFood()
        fun getCategoriesList()
        fun getFoodsListByLetter(letter: String)
        fun searchFoodsList(letter: String)

        fun getFoodByCategory(category: String)
    }
}