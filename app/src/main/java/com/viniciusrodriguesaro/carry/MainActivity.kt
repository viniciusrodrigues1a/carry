package com.viniciusrodriguesaro.carry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.viniciusrodriguesaro.carry.databinding.ActivityMainBinding
import com.viniciusrodriguesaro.carry.ui.AuthViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel.initializeAuth()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
    }

    override fun onStart() {
        super.onStart()

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostMainContent.id) as NavHostFragment

        val currentUser = authViewModel.auth.currentUser
        if (currentUser != null) {
            navHostFragment.navController.graph.setStartDestination(R.id.shoppingItemListFragment)
        } else {
            navHostFragment.navController.graph.setStartDestination(R.id.signinFragment)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostMainContent.id) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}