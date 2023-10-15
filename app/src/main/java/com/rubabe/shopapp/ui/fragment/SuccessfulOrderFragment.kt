package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentProfileBinding
import com.rubabe.shopapp.databinding.FragmentSuccessfulOrderBinding

class SuccessfulOrderFragment : Fragment() {
    private lateinit var binding: FragmentSuccessfulOrderBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_successful_order, container, false)

        binding.successFragment = this
        return binding.root
    }

    fun navigate() {
        findNavController().navigate(R.id.action_successfulOrderFragment_to_mainPageFragment)
    }

}