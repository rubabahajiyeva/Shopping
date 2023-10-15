package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rubabe.shopapp.utils.Extensions.toast
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignInBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        val currentUser = auth.currentUser

        // doesn't show this fragment if user already exists
        if (currentUser != null) {
            findNavController().navigate(R.id.action_signInFragment2_to_splashScreen2)
        }

        binding.signInButton.setOnClickListener {
            val email = binding.emailSignIn.text.toString()
            val password = binding.passwordSignIn.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password)
            } else {
                requireActivity().toast("Enter email and password")
            }
        }

        binding.switchToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment2_to_signUpFragment2)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_signInFragment2_to_splashScreen2)
            }
            .addOnFailureListener {
                requireActivity().toast("Invalid email or password")
            }
    }
}