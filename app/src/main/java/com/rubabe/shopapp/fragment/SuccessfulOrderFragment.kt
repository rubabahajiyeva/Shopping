package com.rubabe.shopapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentProfileBinding
import com.rubabe.shopapp.databinding.FragmentSuccessfulOrderBinding

class SuccessfulOrderFragment : Fragment() {
    private var bindingFragment: FragmentSuccessfulOrderBinding? = null
    private val binding get() = bindingFragment!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingFragment = FragmentSuccessfulOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.orderActualToolbar.title = ""
        binding.orderActualToolbar.setOnClickListener {
            // Inside SuccessFragment

            // Navigate back to MainFragment and clear the back stack
          findNavController().navigate(R.id.action_successfulOrderFragment_to_mainPageFragment)
        }
    }


}