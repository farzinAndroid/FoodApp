package com.example.recipeappmvp.ui.detail

import com.example.foodappmvp.utils.applyIoScheduler
import com.example.recipeappmvp.base.BasePresenterImpl
import com.example.recipeappmvp.data.repository.DetailRepository
import com.example.recipeappmvp.data.repository.HomeRepository
import com.example.recipeappmvp.ui.home.HomeContracts
import javax.inject.Inject

class DetailPresenter @Inject constructor(
    private val repository: DetailRepository,
    val view: DetailContracts.View
) : BasePresenterImpl(), DetailContracts.Presenter {


    override fun getFoodDetails(id: Int) {
        if (view.checkInternet()) {
            view.showFoodsListLoading(true)
            disposable = repository.getFoodDetails(id)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.showFoodsListLoading(false)
                    when (response.code()) {
                        in 200..202 -> {
                            response.body()?.let {
                                if (it.meals!!.isNotEmpty()){
                                    view.showDetail(it)
                                }
                            }
                        }
                    }

                }, {
                    view.showFoodsListLoading(false)
                    view.serverError(it.message.toString())
                })
        }else{
            view.internetError(false)
        }
    }



}