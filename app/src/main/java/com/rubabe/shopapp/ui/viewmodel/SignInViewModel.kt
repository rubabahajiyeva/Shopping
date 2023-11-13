package com.rubabe.shopapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rubabe.shopapp.SignInScreenStateHandle
import com.rubabe.shopapp.di.AppModule
import com.rubabe.shopapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth, private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val errorToastMessage = _toastMessage.asLiveData()


    private val email = savedStateHandle.getStateFlow("EMAIL_KEY", "")
    private val password = savedStateHandle.getStateFlow("PASSWORD_KEY", "")


    val state = combine(email, password) { email, password ->

        SignInScreenStateHandle(
            email = email,
            password = password,
            isActiveButton = email.isNotEmpty() && password.isNotEmpty()
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SignInScreenStateHandle()
    )


    fun setEmail(email: String) {
        savedStateHandle[EMAIL_KEY] = email
    }

    fun setPassword(password: String) {
        savedStateHandle[PASSWORD_KEY] = password
    }


    fun login(email: String, password: String) {
        /*if(email.isNotEmpty() && password.isNotEmpty()) {
            _toastMessage.value = "Successfully"*/
        viewModelScope.launch {
            _login.emit(Resource.Loading())
        }
        firebaseAuth.signInWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            viewModelScope.launch {
                it.user?.let {
                    _login.emit(Resource.Success(it))
                }
            }
        }
            .addOnFailureListener {
                viewModelScope.launch {
                    _login.emit(Resource.Error(it.message.toString()))
                }
            }
        /* }else{
             _toastMessage.value = "Email or password is empty"
         }*/
    }


    companion object {
        private const val EMAIL_KEY = "EMAIL_KEY"
        private const val PASSWORD_KEY = "PASSWORD_KEY"


        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()
                val firebaseAuth = AppModule().providesFirebaseAuth()

                return SignInViewModel(
                    firebaseAuth, savedStateHandle
                ) as T
            }
        }
    }

}