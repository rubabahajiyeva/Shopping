package com.rubabe.shopapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rubabe.shopapp.activity.MainActivity
import com.rubabe.shopapp.databinding.FragmentSplashScreenBinding


class SplashScreen : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to the next screen after the delay
            navigateToNextScreen()
        }, 5000)
    }

    private fun navigateToNextScreen() {

        val intent = Intent(requireContext(), MainActivity::class.java)

        startActivity(intent)

        requireActivity().finish()
    }
}
