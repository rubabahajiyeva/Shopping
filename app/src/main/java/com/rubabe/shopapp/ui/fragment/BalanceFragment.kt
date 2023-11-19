package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentBalanceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceFragment : Fragment() {

    private var _binding: FragmentBalanceBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_balance, container, false)

        binding.profileToolbarHeader = "Balance"

        binding.back.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_balanceFragment_to_profileFragment)
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}