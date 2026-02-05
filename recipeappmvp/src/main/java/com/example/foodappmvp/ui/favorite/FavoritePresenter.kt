package com.example.foodappmvp.ui.favorite

import com.example.foodappmvp.base.BasePresenterImpl
import com.example.foodappmvp.data.repository.DetailRepository
import com.example.foodappmvp.data.repository.FavoriteRepository
import com.example.foodappmvp.ui.detail.DetailContracts
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FavoritePresenter @Inject constructor(
    private val repository: FavoriteRepository,
    val view: FavoriteContracts.View
) : BasePresenterImpl(), FavoriteContracts.Presenter {
    override fun getAllFavoriteFoods() {
        disposable = repository.getAllFoods()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isEmpty()){
                    view.showEmptyList()
                }else{
                    view.showFavoriteList(it)
                }
            }
    }


}