package com.dev.online_todo_list_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        // set up the default AppBar to auto change the correct label
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        // set up the correct navUp() action
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}