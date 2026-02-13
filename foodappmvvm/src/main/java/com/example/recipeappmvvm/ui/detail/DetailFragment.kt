package com.example.recipeappmvvm.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.recipeappmvvm.R
import com.example.recipeappmvvm.data.model.remote.MyResponse
import com.example.recipeappmvvm.databinding.FragmentDetailBinding
import com.example.recipeappmvvm.databinding.FragmentFavoriteBinding
import com.example.recipeappmvvm.utils.CheckConnection
import com.example.recipeappmvvm.utils.PageState
import com.example.recipeappmvvm.utils.setVisibility
import com.example.recipeappmvvm.utils.setupRecyclerView
import com.example.recipeappmvvm.viewmodel.DetailViewmodel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject
import kotlin.getValue


@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding


    @Inject
    lateinit var checkConnection : CheckConnection


    private val navArgs: DetailFragmentArgs by navArgs()
    private var foodId = 0

    private val detailViewmodel : DetailViewmodel by viewModels<DetailViewmodel>()

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

        binding?.apply {

            //back
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            //Check Internet
            checkConnection.observe(viewLifecycleOwner){
                if (it){
                    checkPageState(false, PageState.SUCCESS)
                }else{
                    checkPageState(true, PageState.NETWORK_ERROR)
                }
            }

            //details
            foodId = navArgs.foodId
            detailViewmodel.getFoodDetails(foodId)
            detailViewmodel.foodDetails.observe(viewLifecycleOwner){
                when(it.status){
                    MyResponse.Status.LOADING -> {
                        loading.setVisibility(true, detailContent)
                    }

                    MyResponse.Status.SUCCESS -> {
                        loading.setVisibility(false, detailContent)
                        if (it.data?.meals != null){
                            if (it.data.meals.isNotEmpty()){
                                checkPageState(false, PageState.SUCCESS)

                                val itMeal = it.data.meals[0]
//                                //favorites
//                                foodEntity.apply {
//                                    id = itMeal.idMeal.toString().toInt()
//                                    image = itMeal.strMealThumb.toString()
//                                    title = itMeal.strMeal.toString()
//                                }
//                                presenter.isFoodExists(itMeal.idMeal!!.toInt())
//                                //

                                coverImg.load(itMeal.strMealThumb){
                                    crossfade(true)
                                    crossfade(500)
                                }

                                categoryTxt.text = itMeal.strCategory
                                areaTxt.text = itMeal.strArea

                                if (itMeal.strSource != null){
                                    sourceImg.visibility = View.VISIBLE
                                    sourceImg.setOnClickListener{
                                        Intent(Intent.ACTION_VIEW, itMeal.strSource.toUri()).apply {
                                            startActivity(this)
                                        }
                                    }
                                }else{
                                    sourceImg.visibility = View.GONE
                                }

                                if (itMeal.strYoutube != null){
                                    youtubeImg.visibility = View.VISIBLE
                                }else{
                                    youtubeImg.visibility = View.GONE
                                }

                                titleTxt.text = itMeal.strMeal
                                descTxt.text = itMeal.strInstructions



                                //JsonArray
                                val jsonData = JSONObject(Gson().toJson(it.data))
                                val meals = jsonData.getJSONArray("meals")
                                val meal = meals.getJSONObject(0)

                                //Ingredients
                                for (i in 1..15){
                                    val ingredients = meal.getString("strIngredient$i")
                                    if (ingredients.isNullOrEmpty().not()){
                                        ingredientsTxt.append("$ingredients\n")
                                    }
                                }


                                //Measures
                                for (i in 1..15){
                                    val measures = meal.getString("strMeasure$i")
                                    if (measures.isNullOrEmpty().not()){
                                        measureTxt.append("$measures\n")
                                    }
                                }

                            }
                        }

                    }

                    MyResponse.Status.ERROR -> {
                        loading.setVisibility(true, detailContent)
                    }
                }
            }


        }
    }


    fun checkPageState(error: Boolean,pageState: PageState){
        binding?.apply {
            if (error){
                when(pageState){
                    PageState.EMPTY -> {
                        disconnectLay.disImg.setImageResource(com.example.ui.R.drawable.box)
                        disconnectLay.disTxt.text = getString(com.example.ui.R.string.emptyList)
                    }
                    PageState.NETWORK_ERROR -> {
                        disconnectLay.disImg.setImageResource(com.example.ui.R.drawable.disconnect)
                        disconnectLay.disTxt.text = getString(com.example.ui.R.string.checkInternet)
                    }
                    else -> {}
                }
            }else{
                disconnected.setVisibility(false,detailContent)
            }
        }
    }
}