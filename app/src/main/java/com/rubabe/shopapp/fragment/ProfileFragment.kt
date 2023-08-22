package com.rubabe.shopapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.StorageReference
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentProfileBinding
import com.rubabe.shopapp.model.UserModel


class ProfileFragment : Fragment() {

    private var bindingFragment: FragmentProfileBinding? = null
    private val binding get() = bindingFragment!!
    private lateinit var auth: FirebaseAuth
    private lateinit var username: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingFragment = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        binding.profileActualToolbar.title = "My Profile"

        binding.exitAccount.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.switch_from_profilefragment_to_signinfragment)
        }

        binding.profileActualToolbar.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_mainPageFragment)
        }

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
        bindingFragment = null
    }
}