package com.example.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappmvvm.data.model.database.FoodEntity
import com.example.recipeappmvvm.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewmodel @Inject constructor(private val repository: FavoriteRepository) : ViewModel() {



    val favoriteFoodList = MutableLiveData<List<FoodEntity>>()
    fun getFavoriteFoodsList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllFoods().collect {
            favoriteFoodList.postValue(it)
        }
    }

    fun saveFood(foodEntity: FoodEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveFood(foodEntity)
    }


    fun deleteFood(foodEntity: FoodEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteFood(foodEntity)
    }


    val isFoodExists = MutableLiveData<Boolean>()
    fun isFoodExists(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.isFoodExist(id).collect {
            isFoodExists.postValue(it)
        }
    }


}