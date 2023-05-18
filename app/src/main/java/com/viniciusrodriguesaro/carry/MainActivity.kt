package com.viniciusrodriguesaro.carry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.viniciusrodriguesaro.carry.databinding.ActivityMainBinding
import com.viniciusrodriguesaro.carry.auth.ui.AuthViewModel

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

        authViewModel.currentUser.observe(this) {
            if (it != null) {
                val newNavGraph =
                    navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
                newNavGraph.setStartDestination(R.id.shoppingItemListFragment)
                navHostFragment.navController.graph = newNavGraph
            } else {
                val newNavGraph =
                    navHostFragment.navController.navInflater.inflate(R.navigation.nav_graph)
                newNavGraph.setStartDestination(R.id.signinFragment)
                navHostFragment.navController.graph = newNavGraph
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostMainContent.id) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.signinFragment,
                R.id.shoppingItemListFragment
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}