package com.example.foodappmvp.ui.detail

import com.example.foodappmvp.utils.applyIoScheduler
import com.example.foodappmvp.base.BasePresenterImpl
import com.example.foodappmvp.data.repository.DetailRepository
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