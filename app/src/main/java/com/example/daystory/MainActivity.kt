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
    }

    private fun setupViewModel() {
        val eventRepository = EventRepository(EventDatabase(this))
        val viewModelProviderFactory = EventViewModelFactory(application, eventRepository)
        eventViewModel = ViewModelProvider(this, viewModelProviderFactory)[EventViewModel::class.java]
    }
}
