package com.example.recipeappmvp.ui.detail

import com.example.recipeappmvp.base.BasePresenter
import com.example.recipeappmvp.base.BaseView
import com.example.recipeappmvp.data.model.ResponseFoodList

interface DetailContracts {

    interface View : BaseView{
        fun showDetail(response: ResponseFoodList)
    }


    interface Presenter : BasePresenter{
        fun getFoodDetails(id:Int)
    }

}