package com.example.foodappmvi.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.foodappmvi.data.model.database.FoodEntity
import com.example.foodappmvi.databinding.FragmentDetailBinding
import com.example.foodappmvi.utils.PageState
import com.example.foodappmvi.utils.network.ConnectivityStatus
import com.example.foodappmvi.utils.network.NetworkConnectivityManager
import com.example.foodappmvi.utils.setVisibility
import com.example.foodappmvi.view.detail.DetailIntent
import com.example.foodappmvi.view.detail.DetailState
import com.example.foodappmvi.view.detail.DetailViewmodel
import com.example.foodappmvi.view.home.HomeIntent
import com.example.ui.R
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding


    @Inject
    lateinit var foodEntity: FoodEntity

    val viewmodel : DetailViewmodel by viewModels()

    var isFavorite = false
    var foodId = 0
    val args = navArgs<DetailFragmentArgs>()

    @Inject
    lateinit var networkConnectivityManager: NetworkConnectivityManager



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getId
        foodId = args.value.foodId

        binding?.apply{

            backBtn.setOnClickListener {
                lifecycleScope.launch {
                    viewmodel.detailIntentsChannel.send(DetailIntent.ClosePage)
                }
            }


            // Check internet connectivity
            lifecycleScope.launch {
                networkConnectivityManager.observe().collect { status ->
                    when (status) {
                        ConnectivityStatus.Status.Available -> {
                            checkPageState(false, PageState.SUCCESS)

                            // Send initial intents
                            viewmodel.detailIntentsChannel.send(DetailIntent.GetFoodDetail(foodId))
                            viewmodel.detailIntentsChannel.send(DetailIntent.IsFoodExists(foodId))
                        }
                        ConnectivityStatus.Status.Unavailable,
                        ConnectivityStatus.Status.Losing,
                        ConnectivityStatus.Status.Lost -> {
                            checkPageState(true, PageState.NETWORK_ERROR)
                        }
                    }
                }
            }


            lifecycleScope.launch {

                //observe states
                viewmodel.state.collect {state->
                    when(state){
                        DetailState.ClosePage -> {
                            findNavController().navigateUp()
                        }
                        is DetailState.DeleteFavorite -> {
                            favBtn.setColorFilter(ContextCompat.getColor(requireContext(),R.color.black))
                        }
                        is DetailState.SaveFavorite -> {
                            favBtn.setColorFilter(ContextCompat.getColor(requireContext(),R.color.tartOrange))
                        }
                        is DetailState.SetFavoriteColor -> {
                            isFavorite = state.isFoodInFavorite
                            if (state.isFoodInFavorite){
                                favBtn.setColorFilter(ContextCompat.getColor(requireContext(),R.color.tartOrange))
                            }else{
                                favBtn.setColorFilter(ContextCompat.getColor(requireContext(),R.color.black))
                            }
                        }
                        is DetailState.ShowError -> {
                            loading.setVisibility(false,detailContent)
                            Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                        }
                        is DetailState.ShowFoodDetail -> {
                            loading.setVisibility(false,detailContent)

                            state.food.meals?.get(0)?.let { itMeal->


                                foodEntity.apply {
                                    id = itMeal.idMeal.toString().toInt()
                                    image = itMeal.strMealThumb.toString()
                                    title = itMeal.strMeal.toString()
                                }

                                coverImg.load(itMeal.strMealThumb) {
                                    crossfade(true)
                                    crossfade(500)
                                }

                                categoryTxt.text = itMeal.strCategory
                                areaTxt.text = itMeal.strArea

                                if (itMeal.strSource != null) {
                                    sourceImg.visibility = View.VISIBLE
                                    sourceImg.setOnClickListener {
                                        Intent(Intent.ACTION_VIEW, itMeal.strSource.toUri()).apply {
                                            startActivity(this)
                                        }
                                    }
                                } else {
                                    sourceImg.visibility = View.GONE
                                }

                                if (itMeal.strYoutube != null) {
                                    youtubeImg.visibility = View.VISIBLE
                                } else {
                                    youtubeImg.visibility = View.GONE
                                }

                                titleTxt.text = itMeal.strMeal
                                descTxt.text = itMeal.strInstructions

                                //JsonArray
                                val jsonData = JSONObject(Gson().toJson(state.food))
                                val meals = jsonData.getJSONArray("meals")
                                val meal = meals.getJSONObject(0)

                                //Ingredients
                                for (i in 1..15) {
                                    val ingredients = meal.getString("strIngredient$i")
                                    if (ingredients.isNullOrEmpty().not()) {
                                        ingredientsTxt.append("$ingredients\n")
                                    }
                                }


                                //Measures
                                for (i in 1..15) {
                                    val measures = meal.getString("strMeasure$i")
                                    if (measures.isNullOrEmpty().not()) {
                                        measureTxt.append("$measures\n")
                                    }
                                }
                            }
                        }
                        DetailState.ShowLoading -> {
                            loading.setVisibility(true,detailContent)
                        }
                    }
                }
            }

            // save / delete
            favBtn.setOnClickListener {
                lifecycleScope.launch {
                    if (isFavorite){
                        viewmodel.detailIntentsChannel.send(DetailIntent.DeleteFoodFromFavorite(foodEntity))
                    }else{
                        viewmodel.detailIntentsChannel.send(DetailIntent.SaveFoodToFavorites(foodEntity))
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
                disconnected.setVisibility(true, detailContent)
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
                disconnected.setVisibility(false, detailContent)
            }
        }
    }
}