package com.rubabe.shopapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.rubabe.shopapp.Extensions.toast
import com.rubabe.shopapp.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var signUpEmail: String
    private lateinit var signUpPassword: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignInBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.switch_signInFragment_to_mainFragment)
        }

        val bundle: SignInFragmentArgs by navArgs()
        signUpEmail = bundle.email
        signUpPassword = bundle.password


        binding.signInButton.setOnClickListener {
            signIn()
        }

        binding.switchToSignUp.setOnClickListener{
            findNavController().navigate(R.id.switch_signInFragment_to_signUpFragment)
        }
    }

    private fun signIn() {
        val email = binding.emailSignIn.text.toString()
        val password = binding.passwordSignIn.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Check if the provided email and password match the sign-up values
            if (email == signUpEmail && password == signUpPassword) {
                auth.signInWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener {
                        findNavController().navigate(R.id.switch_signInFragment_to_mainFragment)
                    }
                    .addOnFailureListener {
                        requireActivity().toast("Sign In Fail")
                    }
            } else {
                requireActivity().toast("Email and password do not match.")
            }
        } else {
            requireActivity().toast("Enter email and password")
        }
    }
}