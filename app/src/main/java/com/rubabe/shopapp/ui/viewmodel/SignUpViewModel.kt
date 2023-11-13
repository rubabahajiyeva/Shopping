package com.rubabe.shopapp.ui.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.rubabe.shopapp.data.model.User
import com.rubabe.shopapp.data.model.UserModel
import com.rubabe.shopapp.utils.Constants.USER_COLLECTION
import com.rubabe.shopapp.utils.RegisterFieldsState
import com.rubabe.shopapp.utils.RegisterValidation
import com.rubabe.shopapp.utils.Resource
import com.rubabe.shopapp.utils.validateEmail
import com.rubabe.shopapp.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register

    private val _navigateToSplashScreen = MutableLiveData<Boolean>()
    val navigateToSplashScreen: LiveData<Boolean> = _navigateToSplashScreen

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountEmailAndPassword(email: String, password: String, username: String) {
        if (checkValidation(email, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    viewModelScope.launch {
                        authResult.user?.let { users ->
                            // Add user data to the Realtime Database
                            writeUserDataToDatabase(users.uid, email, username)
                            _register.emit(Resource.Success(users))
                        }
                    }
                    navigateToSplashScreen()
                }
                .addOnFailureListener { exception ->
                    _register.value = Resource.Error(exception.message.toString())
                }
        } else {
            val registerFieldState =
                RegisterFieldsState(validateEmail(email), validatePassword(password))
            runBlocking {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun writeUserDataToDatabase(userId: String, email: String, username: String) {
        val users = UserModel(userId, username, email)

        // Reference to the root of the Realtime Database
        val rootRef = FirebaseDatabase.getInstance().reference

        // Reference to the "users" node in the Realtime Database
        val usersRef = rootRef.child(USER_COLLECTION)

        // Add user data to the "users" node in the Realtime Database
        usersRef.child(userId)
            .setValue(users)
            .addOnSuccessListener {
                // Data written successfully
            }
            .addOnFailureListener { exception ->
                // Handle errors here
            }
    }


    private fun checkValidation(email: String, password: String): Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)

        return (emailValidation is RegisterValidation.Success
                && passwordValidation is RegisterValidation.Success)
    }

    private fun navigateToSplashScreen() {
        _navigateToSplashScreen.value = true
    }
}