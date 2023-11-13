package com.rubabe.shopapp.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
        auth = FirebaseAuth.getInstance()


        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Check if the current destination is DetailsFragment
            if (destination.id == R.id.detailsFragment || destination.id == R.id.successfulOrderFragment) {
                // Hide the bottom navigation
                binding.bottomNav.visibility = View.GONE
            } else {
                // Show the bottom navigation for other destinations
                binding.bottomNav.visibility = View.VISIBLE
            }
        }

    }

/*    override fun onBackPressed() {
        finish() // Finish the activity, which will exit the app
    }*/

}