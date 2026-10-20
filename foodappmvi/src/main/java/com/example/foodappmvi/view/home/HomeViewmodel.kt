package com.example.foodappmvi.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappmvi.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    val intentsChannel = Channel<HomeIntent>()

    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> get() = _state


    init {
        handleIntents()
    }

    private fun handleIntents() = viewModelScope.launch {
        intentsChannel.consumeAsFlow().collect { intent ->
            when (intent) {
                HomeIntent.CreateFilterLetters -> createFiltersLettersList()
                HomeIntent.LoadRandomFoods -> loadRandomFoods()
                HomeIntent.LoadCategoriesList -> loadCategoriesList()
                is HomeIntent.LoadFoodsByCategory -> loadFoodsByCategory(intent.category)
                is HomeIntent.LoadFoodsByLetterList -> loadFoodsByLetter(intent.letter)
                is HomeIntent.LoadSearchFoods -> searchFoods(intent.search)
            }
        }
    }

    private fun searchFoods(search: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _state.emit(HomeState.ShowLoadingFoods)
            val response = homeRepository.searchFoodList(search)
            when (response.code()) {
                in 200..202 -> {
                    _state.emit(
                        if (response.body()?.meals != null) {
                            HomeState.ShowFoodsList(response.body()!!.meals!!)
                        } else {
                            HomeState.ShowEmptySection
                        }
                    )
                }

                in 400..499 -> {
                    _state.emit(HomeState.ShowError(response.message()))
                }

                in 500..599 -> {
                    _state.emit(HomeState.ShowError("500 error"))
                }
            }
        } catch (e: Exception) {
            _state.emit(HomeState.ShowError(e.message ?: "Error occurred"))
        }
    }

    private fun loadFoodsByLetter(letter: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _state.emit(HomeState.ShowLoadingFoods)
            val response = homeRepository.getFoodListByLetter(letter)
            when (response.code()) {
                in 200..202 -> {
                    _state.emit(
                        if (response.body()?.meals != null) {
                            HomeState.ShowFoodsList(response.body()!!.meals!!)
                        } else {
                            HomeState.ShowEmptySection
                        }
                    )
                }

                in 400..499 -> {
                    _state.emit(HomeState.ShowError(response.message()))
                }

                in 500..599 -> {
                    _state.emit(HomeState.ShowError("500 error"))
                }
            }
        } catch (e: Exception) {
            _state.emit(HomeState.ShowError(e.message ?: "Error occurred"))
        }
    }

    private fun loadFoodsByCategory(category: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _state.emit(HomeState.ShowLoadingFoods)
            val response = homeRepository.getFoodsByCategory(category)
            when (response.code()) {
                in 200..202 -> {
                    _state.emit(
                        if (response.body()?.meals != null) {
                            HomeState.ShowFoodsList(response.body()!!.meals!!)
                        } else {
                            HomeState.ShowEmptySection
                        }
                    )
                }

                in 400..499 -> {
                    _state.emit(HomeState.ShowError(response.message()))
                }

                in 500..599 -> {
                    _state.emit(HomeState.ShowError("500 error"))
                }
            }
        } catch (e: Exception) {
            _state.emit(HomeState.ShowError(e.message ?: "Error occurred"))
        }
    }

    private fun loadCategoriesList() = viewModelScope.launch(Dispatchers.IO) {
        try {
            _state.emit(HomeState.ShowLoadingCategory)
            val response = homeRepository.getCategoriesFoodList()
            when (response.code()) {
                in 200..202 -> {
                    if (response.body()?.categories != null) {
                        _state.emit(HomeState.ShowCategoriesList(response.body()!!.categories))
                    } else {
                        _state.emit(HomeState.ShowEmptySection)
                    }
                }

                in 400..499 -> {
                    _state.emit(HomeState.ShowError(response.message()))
                }

                in 500..599 -> {
                    _state.emit(HomeState.ShowError("500 error"))
                }
            }
        } catch (e: Exception) {
            _state.emit(HomeState.ShowError(e.message ?: "Error occurred"))
        }
    }

    private fun loadRandomFoods() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val response = homeRepository.getRandomFood()
            when (response.code()) {
                in 200..202 -> {
                    val meal = response.body()?.meals?.firstOrNull()
                    if (meal != null) {
                        _state.emit(HomeState.ShowRandomFood(meal))
                    }
                }

                in 400..499 -> {
                    _state.emit(HomeState.ShowError(response.message()))
                }

                in 500..599 -> {
                    _state.emit(HomeState.ShowError("500 error"))
                }
            }
        } catch (e: Exception) {
            _state.emit(HomeState.ShowError(e.message ?: "Error occurred"))
        }
    }

    private fun createFiltersLettersList() = viewModelScope.launch {
        val list = listOf('A'..'Z').flatten().toMutableList()
        _state.emit(HomeState.ShowFilterLettersAZ(list))
    }

}