package com.example.foodappmvp.ui.home

import com.example.foodappmvp.utils.applyIoScheduler
import com.example.foodappmvp.base.BasePresenterImpl
import com.example.foodappmvp.data.repository.HomeRepository
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val repository: HomeRepository,
    val view: HomeContracts.View
) : BasePresenterImpl(), HomeContracts.Presenter {
    override fun getRandomFood() {
        if (view.checkInternet()) {
            disposable = repository.getRandomFood()
                .applyIoScheduler()
                .subscribe({ response ->
                    when (response.code()) {
                        in 200..202 -> {
                            response.body()?.let {
                                view.showRandomFood(it)
                            }
                        }

                        422 -> {

                        }

                        in 400..499 -> {

                        }

                        in 500..599 -> {

                        }
                    }

                }, {
                    view.serverError(it.message.toString())
                })
        }
    }

    override fun getCategoriesList() {
        if (view.checkInternet()) {
            view.showLoading()
            disposable = repository.getCategoriesFoodList()
                .applyIoScheduler()
                .subscribe({ response ->
                    view.hideLoading()
                    when (response.code()) {
                        in 200..202 -> {
                            response.body()?.let {
                                if (it.categories.isNotEmpty()){
                                    view.showCategoriesList(it)
                                }
                            }
                        }
                    }

                }, {
                    view.hideLoading()
                    view.serverError(it.message.toString())
                })
        }else{
            view.internetError(false)
        }
    }

    override fun getFoodsListByLetter(letter: String) {
        if (view.checkInternet()) {
            view.showFoodsListLoading(true)
            disposable = repository.getFoodListByLetter(letter)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.showFoodsListLoading(false)
                    when (response.code()) {
                        in 200..202 -> {
                            response.body()?.let {
                                if (it.meals!!.isNotEmpty()){
                                    view.showFoodsList(it)
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

    override fun searchFoodsList(letter: String) {
        if (view.checkInternet()) {
            view.showFoodsListLoading(true)
            disposable = repository.searchFoodList(letter)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.showFoodsListLoading(false)
                    when (response.code()) {
                        in 200..202 -> {
                            response.body()?.let {
                                if (it.meals != null){
                                    if (it.meals.isNotEmpty()){
                                        view.showFoodsList(it)
                                    }
                                }else{
                                    view.showEmptyList()
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

    override fun getFoodByCategory(category: String) {
        if (view.checkInternet()) {
            view.showFoodsListLoading(true)
            disposable = repository.getFoodsByCategory(category)
                .applyIoScheduler()
                .subscribe({ response ->
                    view.showFoodsListLoading(false)
                    when (response.code()) {
                        in 200..202 -> {
                            response.body()?.let {
                                if (it.meals!!.isNotEmpty()){
                                    view.showFoodsList(it)
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