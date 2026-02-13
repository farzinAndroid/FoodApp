package com.example.recipeappmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.data.model.remote.ResponseFoodList
import com.example.recipeappmvvm.data.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewmodel @Inject constructor(private val repository: DetailRepository) : ViewModel() {

    val foodDetails = MutableLiveData<MyResponse<ResponseFoodList>>()
    fun getFoodDetails(foodId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getFoodDetail(foodId).collect {
            foodDetails.postValue(it)
        }
    }





}