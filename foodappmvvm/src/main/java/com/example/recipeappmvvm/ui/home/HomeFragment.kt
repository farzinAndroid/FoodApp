package com.example.recipeappmvvm.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.recipeappmvvm.R
import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.databinding.FragmentHomeBinding
import com.example.recipeappmvvm.utils.setVisibility
import com.example.recipeappmvvm.utils.setupRecyclerView
import com.example.recipeappmvvm.utils.setupSpinnerListWithAdapter
import com.example.recipeappmvvm.viewmodel.HomeViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private val homeViewmodel: HomeViewmodel by viewModels()

    @Inject
    lateinit var categoryAdapter: CategoriesAdapter

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
                        Toast.makeText(requireContext(), letter, Toast.LENGTH_LONG).show()
                    }
                )
            }


            //Categories
            homeViewmodel.getCategoriesFoodList()
            homeViewmodel.categoriesList.observe(viewLifecycleOwner) {
                when (it.status) {
                    MyResponse.Status.LOADING -> {
                        Log.e("TAG","Loading")
                        homeCategoryLoading.setVisibility(true, categoryList)
                    }

                    MyResponse.Status.SUCCESS -> {
                        Log.e("TAG","Success")
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
                        Log.e("TAG","Error")
                        homeCategoryLoading.setVisibility(false, categoryList)
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}