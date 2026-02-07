package com.example.recipeappmvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.recipeappmvvm.R
import com.example.recipeappmvvm.databinding.ActivityFoodBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodActivity : AppCompatActivity() {

    private var _binding : ActivityFoodBinding?=null
    private val binding get() = _binding


    //navhost
    private lateinit var navHost : NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        _binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment


        binding?.apply {

            // setup bottom nav with nav Controller
//            bottomNav.setupWithNavController(navHost.findNavController())
        }
    }

    override fun onNavigateUp(): Boolean {
        return super.onNavigateUp() || navHost.navController.navigateUp()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}