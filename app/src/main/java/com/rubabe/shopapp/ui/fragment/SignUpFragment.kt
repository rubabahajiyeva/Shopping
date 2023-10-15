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
import com.rubabe.shopapp.databinding.FragmentSignUpBinding
import com.rubabe.shopapp.data.model.UserModel

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignUpBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance()
            .getReference("users") // Change this to the appropriate location in your Realtime Database

        val currentUser = auth.currentUser
        /*  if (currentUser != null) {
              findNavController().navigate(R.id.action_signUpFragment2_to_splashScreen2)
          }*/

        binding.signUpButton.setOnClickListener {
            signUp()
        }

        binding.switchToSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment2_to_signInFragment2)
        }
    }

    private fun signUp() {
        val email = binding.emailSignUp.text.toString()
        val password = binding.passwordSignUp1.text.toString()
        val name = binding.usernameSignUp.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener {
                    // Create a user model
                    val userModel: UserModel = UserModel(auth.uid, name, email, password)

                    // Save the user model to the Realtime Database
                    databaseReference.child(auth.uid ?: "" ).setValue(userModel)

                    // Navigate to the sign-in fragment
                    findNavController().navigate(R.id.action_signUpFragment2_to_splashScreen2)
                }
                .addOnFailureListener {
                    requireActivity().toast("Sign Up Fail")
                }
        } else {
            requireActivity().toast("Enter email, password, and username")
        }
    }
}