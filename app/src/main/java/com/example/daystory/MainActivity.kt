package com.example.daystory

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.daystory.database.EventDatabase
import com.example.daystory.repository.EventRepository
import com.example.daystory.viewmodel.EventViewModel
import com.example.daystory.viewmodel.EventViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var eventViewModel: EventViewModel
    private val viewModel by viewModels<EventViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        //bottomNavigationView.selectedItemId = R.id.homeFragment
        bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomePageFragment,
                R.id.loginFragment,
                R.id.firstRegisterFragment,
                R.id.secondRegisterFragment,
                R.id.addEventFragment,
                R.id.editEventFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true)
            .build()

        /*

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.homeFragment, null, navOptions)
                    true
                }
                R.id.navigation_gallery -> {
                    navController.navigate(R.id.galleryFragment, null, navOptions)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.profileFragment, null, navOptions)
                    true
                }
                else -> false
            }
        }

         */
    }

    private fun setupViewModel() {
        val eventRepository = EventRepository(EventDatabase(this))
        val viewModelProviderFactory = EventViewModelFactory(application, eventRepository)
        eventViewModel = ViewModelProvider(this, viewModelProviderFactory)[EventViewModel::class.java]
    }
}
