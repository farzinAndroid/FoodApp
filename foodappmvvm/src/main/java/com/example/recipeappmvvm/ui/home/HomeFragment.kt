package com.example.recipeappmvvm.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.databinding.FragmentHomeBinding
import com.example.recipeappmvvm.ui.detail.DetailFragment
import com.example.recipeappmvvm.utils.CheckConnection
import com.example.recipeappmvvm.utils.PageState
import com.example.recipeappmvvm.utils.setVisibility
import com.example.recipeappmvvm.utils.setupRecyclerView
import com.example.recipeappmvvm.utils.setupSpinnerListWithAdapter
import com.example.recipeappmvvm.viewmodel.HomeViewmodel
import com.example.ui.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val homeViewmodel: HomeViewmodel by viewModels()

    @Inject
    lateinit var categoryAdapter: CategoriesAdapter


    @Inject
    lateinit var foodsListAdapter: FoodsListAdapter

    @Inject
    lateinit var checkConnection : CheckConnection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            //random food
            homeViewmodel.getRandomFoodsList()
            homeViewmodel.randomFoodsListLivedata.observe(viewLifecycleOwner) {
                it[0].let { meal ->
                    headerImg.load(meal.strMealThumb) {
                        crossfade(true)
                        crossfade(500)
                    }
                }
            }


            //Filters
            homeViewmodel.loadCharsFilterList()
            homeViewmodel.charsFilterList.observe(viewLifecycleOwner) {
                filterSpinner.setupSpinnerListWithAdapter(
                    list = it,
                    callback = { letter ->
                        homeViewmodel.getFoodListByLetter(letter)
                        Toast.makeText(requireContext(), letter, Toast.LENGTH_LONG).show()
                    }
                )
            }


            //Categories
            homeViewmodel.getCategoriesFoodList()
            homeViewmodel.categoriesList.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        Log.e("TAG", "Loading")
                        homeCategoryLoading.setVisibility(true, categoryList)
                    }

                    MyResponse.Status.SUCCESS -> {
                        Log.e("TAG", "Success")
                        homeCategoryLoading.setVisibility(false, categoryList)
                        categoryAdapter.setData(it.data!!.categories)
                        categoryList.setupRecyclerView(
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL, false
                            ),
                            adapter = categoryAdapter
                        )

                    }

                    MyResponse.Status.ERROR -> {
                        Log.e("TAG", "Error")
                        homeCategoryLoading.setVisibility(false, categoryList)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            categoryAdapter.setOnItemClickListener {
                homeViewmodel.getFoodsListByCategory(it.strCategory.toString())
            }


            //Foods list by letter
            homeViewmodel.getFoodListByLetter("A")
            homeViewmodel.foodsList.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        homeFoodsLoading.setVisibility(true, foodsList)
                    }

                    MyResponse.Status.SUCCESS -> {
                        homeFoodsLoading.setVisibility(false, foodsList)
                        if (it.data?.meals != null){
                            if (it.data.meals.isNotEmpty()){
                                checkPageState(false, PageState.SUCCESS)
                                foodsListAdapter.setData(it.data.meals)
                                foodsList.setupRecyclerView(
                                    layoutManager = LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL, false
                                    ),
                                    adapter = foodsListAdapter
                                )
                            }
                        }else{
                            checkPageState(true, PageState.EMPTY)
                        }

                    }

                    MyResponse.Status.ERROR -> {
                        homeFoodsLoading.setVisibility(true, foodsList)
                    }
                }
            }


            //Search
            searchEdt.addTextChangedListener {text->
                text?.length?.let { len ->
                    if (len > 2){
                        homeViewmodel.searchFoods(text.toString())
                    }
                }
            }

            //Check Internet
            checkConnection.observe(viewLifecycleOwner){
                if (it){
                    checkPageState(false, PageState.SUCCESS)
                }else{
                    checkPageState(true, PageState.NETWORK_ERROR)
                }
            }


            //Send meal id to detail fragment
            foodsListAdapter.setOnClickListener {
                val direction = HomeFragmentDirections.homeToDetail(it.idMeal!!.toInt())
                findNavController().navigate(direction)
            }
        }
    }


    fun checkPageState(error: Boolean,pageState: PageState){
         binding?.apply {
             if (error){
                 when(pageState){
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
             }else{
                 homeDisLay.setVisibility(false,homeContent)
             }
         }
    }



}