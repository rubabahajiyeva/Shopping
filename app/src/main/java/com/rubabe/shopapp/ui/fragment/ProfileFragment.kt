package com.rubabe.shopapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.rubabe.shopapp.ui.activity.SignActivity
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.profileFragment = this
        binding.profileToolbarHeader = "My Profile"

        binding.goToEditProfile.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.goToAboutApp.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_aboutAppFragment)
        }

        binding.goToHelp.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_helpFragment)
        }

        binding.goToBalance.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_balanceFragment)
        }

        binding.goToSettings.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        binding.goToPrivacyAndPolicy.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_privacyAndPolicyFragment)
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

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            databaseReference.child("users").child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserModel::class.java)
                        user?.let {
                            binding.usernameTV.text = user.userName
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle database read error
                    }
                }
            )
        }
    }

    fun exitAccount() {
        auth.signOut()
        val intent = Intent(requireContext(), SignActivity::class.java)
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


