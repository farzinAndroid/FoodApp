package com.example.foodappmvp.ui.detail

import com.example.foodappmvp.utils.applyIoScheduler
import com.example.foodappmvp.base.BasePresenterImpl
import com.example.foodappmvp.data.model.database.FoodEntity
import com.example.foodappmvp.data.repository.DetailRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
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

    override fun saveFood(foodEntity: FoodEntity) {
        disposable = repository.saveFood(foodEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.updateFavorite(true)
            }
    }

    override fun deleteFood(foodEntity: FoodEntity) {
        disposable = repository.deleteFood(foodEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.updateFavorite(false)
            }
    }

    override fun isFoodExists(foodId: Int) {
        disposable = repository.isFoodExist(foodId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.updateFavorite(it)
            }
    }


}