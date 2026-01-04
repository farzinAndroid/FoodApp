package com.example.foodappmvp.ui.detail

import com.example.foodappmvp.base.BasePresenter
import com.example.foodappmvp.base.BaseView
import com.example.foodappmvp.data.model.ResponseFoodList

interface DetailContracts {

    interface View : BaseView{
        fun showDetail(response: ResponseFoodList)
    }


    interface Presenter : BasePresenter{
        fun getFoodDetails(id:Int)
    }

}