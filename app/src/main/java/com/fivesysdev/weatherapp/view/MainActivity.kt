package com.fivesysdev.weatherapp.view

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.fivesysdev.weatherapp.R
import com.fivesysdev.weatherapp.databinding.ActivityMainBinding
import com.fivesysdev.weatherapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    var fabClickListener: (() -> (Unit))? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.haveFavourite()
        viewModel.haveFavouriteLiveData.observe(this) {
            binding.bottomNavigationView.isVisible = it
        }

        binding.fabAdd.setOnClickListener {
            fabClickListener?.invoke()
        }
        val navController = findNavController(R.id.nav_host_fragment_content_favourite)
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.weatherFragment)
                }
                R.id.favourites -> {
                    navController.navigate(R.id.favouriteFragment)
                }
            }
            true

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_favourite)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun showBottomNavigation() {
        binding.bottomNavigationView.isVisible = true
    }
}