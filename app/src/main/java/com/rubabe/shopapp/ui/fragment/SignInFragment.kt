package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentSignInBinding
import com.rubabe.shopapp.ui.viewmodel.SignInViewModel
import com.rubabe.shopapp.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<SignInViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            findNavController().navigate(R.id.action_signInFragment2_to_splashScreen2)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.emailSignIn.text.toString().trim()
            val password = binding.passwordSignIn.text.toString()

            viewModel.login(email, password)
        }


        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.btnSignIn.isEnabled = state.isActiveButton
            }
        }

        binding.switchToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment2_to_signUpFragment2)
        }

        binding.forgetPasswordTV.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgetFragment)
        }






        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            findNavController().navigate(R.id.action_signInFragment2_to_splashScreen2)
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Password or Email is wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }
        }


        binding.emailSignIn.doAfterTextChanged {
            viewModel.setEmail(it?.toString() ?: "")
        }

        binding.passwordSignIn.doAfterTextChanged {
            viewModel.setPassword(it?.toString() ?: "")
        }

    }


    override fun onResume() {
        super.onResume()
        binding.emailSignIn.setText(viewModel.state.value.email)
        binding.passwordSignIn.setText(viewModel.state.value.password)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}