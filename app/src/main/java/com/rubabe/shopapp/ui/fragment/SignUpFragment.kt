package com.rubabe.shopapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.rubabe.shopapp.data.model.User
import com.rubabe.shopapp.ui.viewmodel.SignUpViewModel
import com.rubabe.shopapp.utils.RegisterValidation
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            signUpButton.setOnClickListener {
                val user = User(
                    usernameSignUp.text.toString().trim(),
                    emailSignUp.text.toString().trim()
                )

                val password = passwordSignUp1.text.toString()
                val email = emailSignUp.text.toString()
                val username = usernameSignUp.text.toString()
                viewModel.createAccountEmailAndPassword(email, password, username)
            }

            switchToSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment2_to_signInFragment2)
            }
        }
        viewModel.navigateToSplashScreen.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_signUpFragment2_to_splashScreen2)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.validation.collect { validation ->
                    if (validation.email is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.emailSignUp.apply {
                                requestFocus()
                                error = validation.email.message
                            }
                        }
                    }

                    if (validation.password is RegisterValidation.Failed) {
                        withContext(Dispatchers.Main) {
                            binding.passwordSignUp1.apply {
                                requestFocus()
                                error = validation.password.message
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}