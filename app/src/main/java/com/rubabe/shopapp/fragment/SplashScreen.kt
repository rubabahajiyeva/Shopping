package com.rubabe.shopapp.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentSplashScreenBinding


class SplashScreen : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up any animations or transitions here

        // Simulate a delay (e.g., 2 seconds) to showcase the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to the next screen after the delay
            navigateToNextScreen()
        }, 5000)
    }

    private fun navigateToNextScreen() {
        // Use the NavController to navigate to the next fragment
        findNavController().navigate(R.id.action_splashScreen_to_mainPageFragment)
    }
}
