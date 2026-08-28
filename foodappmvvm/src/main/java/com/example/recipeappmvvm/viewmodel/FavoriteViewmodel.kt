package com.example.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappmvvm.data.model.database.FoodEntity
import com.example.recipeappmvvm.data.model.database.MyResponseDB
import com.example.recipeappmvvm.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewmodel @Inject constructor(private val repository: FavoriteRepository) : ViewModel() {



    val favoriteFoodList = MutableLiveData<MyResponseDB<List<FoodEntity>>>()
    fun getFavoriteFoodsList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllFoods().collect {
            favoriteFoodList.postValue(MyResponseDB.success(it))
        }
    }


}