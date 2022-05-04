package com.dev.online_todo_list_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.dev.online_todo_list_example.utils.hideKeyboard

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val navHostFragment = fragment as NavHostFragment
        navController = navHostFragment.navController

        // Set up the default AppBar to auto change the correct label
        setupActionBarWithNavController(navController)

        // Hide soft Keyboard
        hideKeyboard(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Set up the correct navUp() action
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}