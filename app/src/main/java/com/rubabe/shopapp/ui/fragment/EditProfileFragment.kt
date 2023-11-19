package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rubabe.shopapp.data.model.UserModel
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    //private lateinit var viewModel: ProfileViewModel

    private var auth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)

        binding.profileToolbarHeader = "Edit Profile"

        binding.back.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_editProfileFragment_to_profileFragment)
        }



        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val tempViewModel: ProfileViewModel by viewModels()
        viewModel = tempViewModel*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        databaseReference = FirebaseDatabase.getInstance().reference


        // Retrieve and display the username
        val userId = auth.currentUser?.uid
        if (userId != null) {
            databaseReference.child("users").child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserModel::class.java)
                        user?.let {
                            // Set the retrieved username to your TextView or UI element
                            binding.usernameTV.text = user.userName
                            binding.emailTV.text = user.userEmail
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database read error
                    }
                }
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}