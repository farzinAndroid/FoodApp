package com.example.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.data.model.remote.ResponseCategoriesList
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import com.example.recipeappmvvm.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(private val repository: HomeRepository) : ViewModel() {


    val randomFoodsListLivedata = MutableLiveData<List<ResponseFoodList.Meal>>()
    fun getRandomFoodsList() = viewModelScope.launch {
        repository.getRandomFood().collect {
            randomFoodsListLivedata.postValue(it.body()!!.meals!!)
        }
    }

    val categoriesList = MutableLiveData<MyResponse<ResponseCategoriesList>>()
    fun getCategoriesFoodList() = viewModelScope.launch(Dispatchers.IO) {
        repository.getCategoriesFoodList().collect {
            categoriesList.postValue(it)
        }
    }


    val charsFilterList = MutableLiveData<MutableList<Char>>()
    fun loadCharsFilterList() = viewModelScope.launch {
        val filters = listOf('A'..'Z').flatten().toMutableList()
        charsFilterList.postValue(filters)
    }




    val foodsList = MutableLiveData<MyResponse<ResponseFoodList>>()
    fun getFoodListByLetter(letter: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.getFoodListByLetter(letter).collect {
            foodsList.postValue(it)
        }
    }



    fun searchFoods(search: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.searchFoods(search).collect {
            foodsList.postValue(it)
        }
    }


    fun getFoodsListByCategory(category: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.getFoodListByCategory(category).collect {
            foodsList.postValue(it)
        }
    }

}