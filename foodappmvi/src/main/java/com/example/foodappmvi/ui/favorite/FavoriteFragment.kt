package com.example.foodappmvi.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappmvi.databinding.FragmentFavoriteBinding
import com.example.foodappmvi.ui.home.HomeFragmentDirections
import com.example.foodappmvi.utils.setVisibility
import com.example.foodappmvi.utils.setupRecyclerView
import com.example.foodappmvi.view.favorite.FavoriteIntent
import com.example.foodappmvi.view.favorite.FavoriteState
import com.example.foodappmvi.view.favorite.FavoriteViewmodel
import com.example.ui.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding


    @Inject
    lateinit var favoriteAdapter : FavoriteListAdapter

    private val viewmodel : FavoriteViewmodel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {

            //intents
            lifecycleScope.launch {
                //send
                viewmodel.favoriteIntentsChannel.send(FavoriteIntent.LoadFavorites)

                //observe
                viewmodel.state.collect { state ->
                    when(state){
                        is FavoriteState.ShowFavoritesList ->{
                            emptyListLay.isVisible = false
                            favList.isVisible = true

                            favoriteAdapter.setData(state.favorites)
                            favList.setupRecyclerView(
                                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false),
                                favoriteAdapter
                            )

                            favoriteAdapter.setOnClickListener {
                                val direction = HomeFragmentDirections.homeToDetail(it.id)
                                findNavController().navigate(direction)
                            }
                        }
                        is FavoriteState.ShowEmpty -> {
                            favList.isVisible = false
                            emptyListLay.isVisible = true
                        }
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}