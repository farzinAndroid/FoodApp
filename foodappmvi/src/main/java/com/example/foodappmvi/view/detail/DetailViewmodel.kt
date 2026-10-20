package com.example.foodappmvi.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappmvi.data.model.database.FoodEntity
import com.example.foodappmvi.data.repository.DetailRepository
import com.example.foodappmvi.view.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewmodel @Inject constructor(
    private val repository: DetailRepository
) : ViewModel() {

    val detailIntentsChannel = Channel<DetailIntent>()

    private val _state = MutableStateFlow<DetailState>(DetailState.ShowLoading)
    val state = _state.asStateFlow()


    init {
        handleIntents()
    }

    private fun handleIntents() = viewModelScope.launch{
        detailIntentsChannel.consumeAsFlow().collect {intent->
            when(intent){
                DetailIntent.ClosePage -> closePage()
                is DetailIntent.DeleteFoodFromFavorite -> deleteFoodFromFavorite(intent.foodEntity)
                is DetailIntent.GetFoodDetail -> getFoodDetail(intent.id)
                is DetailIntent.IsFoodExists -> isFoodExists(intent.id)
                is DetailIntent.SaveFoodToFavorites -> saveFoodToFavorites(intent.foodEntity)
            }
        }
    }

    private fun saveFoodToFavorites(foodEntity: FoodEntity) = viewModelScope.launch {
        _state.emit(DetailState.SaveFavorite(repository.saveFood(foodEntity)))
    }

    private fun isFoodExists(id: Int) = viewModelScope.launch {
        repository.isFoodExist(id).collect {
            _state.emit(DetailState.SetFavoriteColor(it))
        }
    }

    private fun getFoodDetail(id: Int) = viewModelScope.launch {
        val response = repository.getFoodDetails(id)
        _state.emit(DetailState.ShowLoading)
        when(response.code()){
            in 200..202 -> {
                _state.emit(DetailState.ShowFoodDetail(response.body()!!))
            }

            in 400..499 -> {
                _state.emit(DetailState.ShowError(response.message()))
            }

            in 500..599 -> {
                _state.emit(DetailState.ShowError("500 error"))
            }
        }
    }

    private fun deleteFoodFromFavorite(foodEntity: FoodEntity) = viewModelScope.launch {
        _state.emit(DetailState.DeleteFavorite(repository.deleteFood(foodEntity)))
    }

    private fun closePage() = viewModelScope.launch {
        _state.emit(DetailState.ClosePage)
    }


}