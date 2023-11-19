package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentPrivacyAndPolicyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyAndPolicyFragment : Fragment() {

    private var _binding: FragmentPrivacyAndPolicyBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_privacy_and_policy, container, false)

        binding.profileToolbarHeader = "Privacy And Policy"

        binding.back.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_privacyAndPolicyFragment_to_profileFragment)
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}