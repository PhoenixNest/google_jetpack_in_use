package com.dev.retrofit_in_use.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dev.retrofit_in_use.R
import com.dev.retrofit_in_use.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val navHostFragment = fragment as NavHostFragment
        navController = navHostFragment.navController

        // Set up the default AppBar to auto change the correct label
        setupActionBarWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(
            // Like the Route Table in Flutter,
            // Use this setOf() function to appoint the Fragment that you want to Navigate with BottomNavigationView
            setOf(
                R.id.pixabayFragment,
                R.id.compareFragment,
            )
        )

        // Binding the navController with bottomNavigationView
        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Auto-Change the Title of each Fragment
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}