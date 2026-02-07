package com.example.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import com.example.recipeappmvvm.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(private val repository: HomeRepository) : ViewModel() {


    val randomFoodsListLivedata = MutableLiveData<List<ResponseFoodList.Meal>>()

    fun getRandomFoodsList() = viewModelScope.launch {
        repository.getRandomFood().collect {
            randomFoodsListLivedata.postValue(it.body()!!.meals!!)
        }
    }


    val charsFilterList = MutableLiveData<MutableList<Char>>()
    fun loadCharsFilterList() = viewModelScope.launch {
        val filters = listOf('A'..'Z').flatten().toMutableList()
        charsFilterList.postValue(filters)
    }

}