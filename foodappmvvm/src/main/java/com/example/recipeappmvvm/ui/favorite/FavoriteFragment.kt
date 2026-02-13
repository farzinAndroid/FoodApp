package com.example.recipeappmvvm.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeappmvvm.R
import com.example.recipeappmvvm.databinding.FragmentFavoriteBinding
import com.example.recipeappmvvm.databinding.FragmentHomeBinding
import com.example.recipeappmvvm.ui.home.HomeFragmentDirections
import com.example.recipeappmvvm.viewmodel.FavoriteViewmodel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding


    @Inject
    lateinit var favoriteAdapter: FavoriteListAdapter


    private val favoriteViewmodel by viewModels<FavoriteViewmodel>()

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

            favoriteViewmodel.getFavoriteFoodsList()
            favoriteViewmodel.favoriteFoodList.observe(viewLifecycleOwner){list->
                if (list.isNotEmpty()){
                    favoriteAdapter.setData(list)
                    favList.visibility = View.VISIBLE
                    emptyListLay.visibility = View.GONE
                    favList.adapter = favoriteAdapter
                    favList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

                }else{
                    favList.visibility = View.GONE
                    emptyListLay.visibility = View.VISIBLE
                }

            }




            favoriteAdapter.setOnClickListener {
                val direction = HomeFragmentDirections.homeToDetail(it.id)
                findNavController().navigate(direction)
            }


        }
    }

}