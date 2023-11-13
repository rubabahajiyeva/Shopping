package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentForgetBinding


class ForgetFragment : Fragment() {
    private var _binding: FragmentForgetBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    lateinit var email:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgetBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.forgetButton.setOnClickListener {
            validateData()
        }

        binding.backToLogin.setOnClickListener{

            findNavController().navigate(R.id.action_forgetFragment_to_signInFragment)
        }

        return binding.root
    }

    private fun validateData() {
        println("test")
        email = binding.edtForget.text.toString()
        if (email.isEmpty()) {
            println("test1")
            binding.edtForget.error = "Required"
        } else {
            println("test2")
            forgetPass()
        }
    }


    private fun forgetPass() {
        println("test3")
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("test4")
                Toast.makeText(requireContext(), "Check your email", Toast.LENGTH_LONG).show()
            } else {
                println("test5")
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}