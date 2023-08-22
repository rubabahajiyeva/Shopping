package com.rubabe.shopapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rubabe.shopapp.utils.Extensions.toast
import com.rubabe.shopapp.R
import com.rubabe.shopapp.fragment.SignInFragmentArgs
import com.rubabe.shopapp.databinding.FragmentSignInBinding
import com.rubabe.shopapp.model.UserModel

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
        if (currentUser != null) {
            findNavController().navigate(R.id.switch_signInFragment_to_mainFragment)
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
            findNavController().navigate(R.id.switch_signInFragment_to_signUpFragment)
        }
    }

    private fun validateSignInCredentials(email: String, password: String) {
        // Fetch user data based on provided email
        databaseReference.child("users").orderByChild("userEmail").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(UserModel::class.java)
                            /*  if (user?.userPassword == password) {
                                  signIn(email, password)
                                  return
                              } */
                        }
                        requireActivity().toast("Invalid email or password")
                    } else {
                        requireActivity().toast("Invalid email or password")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    requireActivity().toast("Error occurred while signing in")
                }
            })
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                findNavController().navigate(R.id.switch_signInFragment_to_mainFragment)
            }
            .addOnFailureListener {
                requireActivity().toast("Sign In Fail")
            }
    }
}
