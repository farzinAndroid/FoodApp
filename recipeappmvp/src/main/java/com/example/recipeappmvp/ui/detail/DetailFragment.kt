package com.example.recipeappmvp.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.foodappmvp.utils.isNetworkAvailable
import com.example.foodappmvp.utils.showSnackBar
import com.example.recipeappmvp.R
import com.example.recipeappmvp.data.model.ResponseFoodList
import com.example.recipeappmvp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import greyfox.rxnetwork.RxNetwork
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class DetailFragment : Fragment(), DetailContracts.View {

    private lateinit var binding: FragmentDetailBinding

    @Inject
    lateinit var presenter: DetailPresenter


    private var foodId = 0
    private val bundle : DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root

    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            foodId = bundle.foodId
            if (foodId > 0){
                // call api
                presenter.getFoodDetails(foodId)
            }


        //check internet
        RxNetwork.init(requireContext())
            .observe()
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe {
                internetError(it.isConnected)
            }
        //back click
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun showDetail(response: ResponseFoodList) {
        binding.apply {
            response.meals?.get(0)?.let {itMeal->
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
                val jsonData = JSONObject(Gson().toJson(response))
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


    override fun showLoading() {
        binding.apply {
            loading.visibility = View.VISIBLE
            detailContent.visibility = View.GONE
        }
    }

    override fun hideLoading() {
        binding.apply {
            loading.visibility = View.GONE
            detailContent.visibility = View.VISIBLE
        }
    }

    override fun showFoodsListLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                loading.visibility = View.VISIBLE
                detailContent.visibility = View.GONE
            } else {
                loading.visibility = View.GONE
                detailContent.visibility = View.VISIBLE
            }
        }
    }

    override fun checkInternet(): Boolean {
        return requireContext().isNetworkAvailable()
    }

    override fun internetError(hasInternet: Boolean) {
        binding.apply {
            if (!hasInternet){
                detailContent.visibility = View.GONE
                disconnected.visibility = View.VISIBLE

                disconnectLay.disImg.setImageResource(com.example.ui.R.drawable.disconnect)
                disconnectLay.disTxt.text = getString(com.example.ui.R.string.checkInternet)
            }else{
                detailContent.visibility = View.VISIBLE
                disconnected.visibility = View.GONE

                presenter.getFoodDetails(foodId)
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