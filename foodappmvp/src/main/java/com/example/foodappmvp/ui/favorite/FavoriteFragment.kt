package com.example.foodappmvp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappmvp.R
import com.example.foodappmvp.data.model.database.FoodEntity
import com.example.foodappmvp.data.repository.FavoriteRepository
import com.example.foodappmvp.databinding.FragmentDetailBinding
import com.example.foodappmvp.databinding.FragmentFavoriteBinding
import com.example.foodappmvp.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment(), FavoriteContracts.View {

    private lateinit var binding: FragmentFavoriteBinding

    @Inject
    lateinit var presenter: FavoritePresenter

    @Inject
    lateinit var favoriteAdapter: FavoriteListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.getAllFavoriteFoods()

    }

    override fun showEmptyList() {
        binding.apply {
            favList.visibility = View.GONE
            emptyListLay.visibility = View.VISIBLE
        }
    }

    override fun showFavoriteList(list: MutableList<FoodEntity>) {
        favoriteAdapter.setData(list)
        binding.apply {
            favList.visibility = View.VISIBLE
            emptyListLay.visibility = View.GONE

            favList.adapter = favoriteAdapter
            favList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        }

        favoriteAdapter.setOnClickListener {
            val direction = HomeFragmentDirections.homeToDetail(it.id)
            findNavController().navigate(direction)
        }


    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

}