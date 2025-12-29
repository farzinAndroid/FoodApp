package com.example.recipeappmvp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.foodappmvp.utils.isNetworkAvailable
import com.example.foodappmvp.utils.showSnackBar
import com.example.recipeappmvp.R
import com.example.recipeappmvp.data.model.ResponseCategoriesList
import com.example.recipeappmvp.data.model.ResponseFoodList
import com.example.recipeappmvp.databinding.FragmentHomeBinding
import com.example.recipeappmvp.ui.home.adapter.CategoriesListAdapter
import com.example.recipeappmvp.ui.home.adapter.FoodsListAdapter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import greyfox.rxnetwork.RxNetwork
import io.reactivex.Scheduler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeContracts.View {

    //Binding
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var presenter: HomePresenter

    @Inject
    lateinit var categoriesListAdapter: CategoriesListAdapter

    @Inject
    lateinit var foodsListAdapter: FoodsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Call api
            presenter.getRandomFood()
            presenter.getCategoriesList()
            presenter.getFoodsListByLetter("A")
            //Search
            searchEdt.textChanges()
                .skipInitialValue()
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.toString().length > 1) {
                        //Call Api
                        presenter.searchFoodsList(it.toString())
                    }
                }


            //Filter By letter Section
            createFilterFoodSpinnerList()

            //check internet
            RxNetwork.init(requireContext())
                .observe()
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe {
                    internetError(it.isConnected)
                }
        }
    }


    private fun createFilterFoodSpinnerList() {
        val filters = listOf('A'..'Z').flatten()
        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner, filters)
        adapter.setDropDownViewResource(R.layout.item_spinner_list)
        binding.filterSpinner.adapter = adapter
        binding.filterSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                presenter.getFoodsListByLetter(filters[position].toString())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    override fun showRandomFood(data: ResponseFoodList) {
        binding.headerImg.load(data.meals?.get(0)?.strMealThumb)
    }

    override fun showCategoriesList(categoriesList: ResponseCategoriesList) {
        binding.categoryList.apply {
            categoriesListAdapter.setData(categoriesList.categories)
            adapter = categoriesListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        categoriesListAdapter.setOnClickListener {
            presenter.getFoodByCategory(it.strCategory.toString())
        }
    }

    override fun showFoodsList(foodsList: ResponseFoodList) {


        binding.foodsList.visibility = View.VISIBLE
        binding.homeDisLay.visibility = View.GONE



        binding.foodsList.apply {
            foodsListAdapter.setData(foodsList.meals!!)
            adapter = foodsListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        foodsListAdapter.setOnClickListener {
            val direction = HomeFragmentDirections.homeToDetail(it.idMeal!!.toInt())
            findNavController().navigate(direction)
        }
    }

    override fun showEmptyList() {
        binding.apply {
            foodsList.visibility = View.GONE
            homeDisLay.visibility = View.VISIBLE

            disconnectLay.disImg.setImageResource(com.example.ui.R.drawable.box)
            disconnectLay.disTxt.text = getString(com.example.ui.R.string.emptyList)
        }
    }

    override fun showLoading() {
        binding.apply {
            homeCategoryLoading.visibility = View.VISIBLE
            categoryList.visibility = View.GONE
        }
    }

    override fun hideLoading() {
        binding.apply {
            homeCategoryLoading.visibility = View.GONE
            categoryList.visibility = View.VISIBLE
        }
    }

    override fun showFoodsListLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                homeFoodsLoading.visibility = View.VISIBLE
                foodsList.visibility = View.GONE
            } else {
                homeFoodsLoading.visibility = View.GONE
                foodsList.visibility = View.VISIBLE
            }
        }
    }

    override fun checkInternet(): Boolean {
        return requireContext().isNetworkAvailable()
    }

    override fun internetError(hasInternet: Boolean) {
        binding.apply {
            if (!hasInternet){
                homeContent.visibility = View.GONE
                homeDisLay.visibility = View.VISIBLE

                disconnectLay.disImg.setImageResource(com.example.ui.R.drawable.disconnect)
                disconnectLay.disTxt.text = getString(com.example.ui.R.string.checkInternet)
            }else{
                homeContent.visibility = View.VISIBLE
                homeDisLay.visibility = View.GONE

                presenter.getCategoriesList()
                presenter.getFoodsListByLetter("A")
            }

        }

    }

    override fun serverError(message: String) {
        binding.root.showSnackBar(message)
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }
}