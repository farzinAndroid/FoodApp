package com.example.foodappmvi.view.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappmvi.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewmodel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {

    val favoriteIntentsChannel = Channel<FavoriteIntent>()

    val _state = MutableStateFlow<FavoriteState>(FavoriteState.ShowEmpty)
    val state = _state.asStateFlow()


    init {
        handleIntent()
    }

    fun handleIntent() = viewModelScope.launch {
        favoriteIntentsChannel.consumeAsFlow().collect {
            when (it) {
                FavoriteIntent.LoadFavorites -> loadFavorites()
            }
        }
    }

    private fun loadFavorites() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllFoods().collect {
            _state.emit(
                if (it.isEmpty())
                    FavoriteState.ShowEmpty
                else
                    FavoriteState.ShowFavoritesList(it)
            )


        }
    }


}