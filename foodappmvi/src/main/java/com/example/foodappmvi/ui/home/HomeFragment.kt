package com.example.foodappmvi.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.foodappmvi.databinding.FragmentHomeBinding
import com.example.foodappmvi.utils.PageState
import com.example.foodappmvi.utils.network.ConnectivityStatus
import com.example.foodappmvi.utils.network.NetworkConnectivityManager
import com.example.foodappmvi.utils.setVisibility
import com.example.foodappmvi.utils.setupRecyclerView
import com.example.foodappmvi.utils.setupSpinnerListWithAdapter
import com.example.foodappmvi.view.home.HomeIntent
import com.example.foodappmvi.view.home.HomeState
import com.example.foodappmvi.view.home.HomeViewmodel
import com.example.ui.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val viewmodel: HomeViewmodel by viewModels()

    @Inject
    lateinit var categoryAdapter: CategoriesAdapter

    @Inject
    lateinit var foodsAdapter: FoodsListAdapter

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {

            // Check internet connectivity
            lifecycleScope.launch {
                networkConnectivityManager.observe().collect { status ->
                    when (status) {
                        ConnectivityStatus.Status.Available -> {
                            checkPageState(false, PageState.SUCCESS)

                            // Send initial intents
                            viewmodel.intentsChannel.send(HomeIntent.CreateFilterLetters)
                            viewmodel.intentsChannel.send(HomeIntent.LoadRandomFoods)
                            viewmodel.intentsChannel.send(HomeIntent.LoadCategoriesList)
                            viewmodel.intentsChannel.send(HomeIntent.LoadFoodsByLetterList("A"))
                        }
                        ConnectivityStatus.Status.Unavailable,
                        ConnectivityStatus.Status.Losing,
                        ConnectivityStatus.Status.Lost -> {
                            checkPageState(true, PageState.NETWORK_ERROR)
                        }
                    }
                }
            }

            // Receive UI state changes
            lifecycleScope.launch {
                viewmodel.state.collect { homeState ->
                    when (homeState) {
                        HomeState.Idle -> {}
                        is HomeState.ShowError -> {
                            Toast.makeText(
                                requireContext(),
                                homeState.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is HomeState.ShowFilterLettersAZ -> {
                            filterSpinner.setupSpinnerListWithAdapter(homeState.lettersAZ) { letter ->
                                lifecycleScope.launch {
                                    viewmodel.intentsChannel.send(
                                        HomeIntent.LoadFoodsByLetterList(letter)
                                    )
                                }
                            }
                        }

                        is HomeState.ShowRandomFood -> {
                            if (homeState.food != null) {
                                headerImg.load(homeState.food.strMealThumb) {
                                    crossfade(true)
                                    crossfade(500)
                                }
                            }
                        }

                        is HomeState.ShowCategoriesList -> {
                            checkPageState(false, PageState.SUCCESS)
                            homeCategoryLoading.setVisibility(false, categoryList)

                            categoryAdapter.setData(homeState.categoriesList)
                            categoryList.setupRecyclerView(
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL, false
                                ),
                                adapter = categoryAdapter
                            )

                            categoryAdapter.setOnItemClickListener { category ->
                                lifecycleScope.launch {
                                    viewmodel.intentsChannel.send(
                                        HomeIntent.LoadFoodsByCategory(category.strCategory!!)
                                    )
                                }
                            }
                        }

                        HomeState.ShowEmptySection -> {
                            checkPageState(true, PageState.EMPTY)
                        }

                        is HomeState.ShowFoodsList -> {
                            checkPageState(false, PageState.SUCCESS)
                            homeFoodsLoading.setVisibility(false, foodsList)

                            foodsAdapter.setData(homeState.foodsList)

                            foodsList.setupRecyclerView(
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL, false
                                ),
                                adapter = foodsAdapter
                            )

                            foodsAdapter.setOnClickListener { food ->
                                val direction =
                                    HomeFragmentDirections.homeToDetail(food.idMeal?.toInt()!!)
                                findNavController().navigate(direction)
                            }
                        }

                        HomeState.ShowLoadingCategory -> {
                            homeCategoryLoading.setVisibility(true, categoryList)
                        }

                        HomeState.ShowLoadingFoods -> {
                            homeFoodsLoading.setVisibility(true, foodsList)
                        }
                    }
                }
            }

            // Search text listener (registered outside the endless collect coroutine)
            searchEdt.addTextChangedListener { editable ->
                val query = editable.toString().trim()
                if (query.length > 2) {
                    lifecycleScope.launch {
                        viewmodel.intentsChannel.send(HomeIntent.LoadSearchFoods(query))
                    }
                } else if (query.isEmpty()) {
                    lifecycleScope.launch {
                        viewmodel.intentsChannel.send(HomeIntent.LoadFoodsByLetterList("A"))
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun checkPageState(error: Boolean, pageState: PageState) {
        binding?.apply {
            if (error) {
                homeDisLay.setVisibility(true, homeContent)
                when (pageState) {
                    PageState.EMPTY -> {
                        disconnectLay.disImg.setImageResource(R.drawable.box)
                        disconnectLay.disTxt.text = getString(R.string.emptyList)
                    }

                    PageState.NETWORK_ERROR -> {
                        disconnectLay.disImg.setImageResource(R.drawable.disconnect)
                        disconnectLay.disTxt.text = getString(R.string.checkInternet)
                    }

                    else -> {}
                }
            } else {
                homeDisLay.setVisibility(false, homeContent)
            }
        }
    }
}