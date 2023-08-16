package com.rubabe.shopapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.model.CardModel

class CardViewModel : ViewModel() {

    private val orderDatabaseReference = Firebase.firestore.collection("orders")
    private val auth = FirebaseAuth.getInstance()

    // LiveData for cart items
    private val _cartItems = MutableLiveData<List<CardModel>>()
    val cartItems: LiveData<List<CardModel>> = _cartItems

    // Function to retrieve cart items
    fun retrieveCartItems() {
        // Fetch and update _cartItems LiveData
        orderDatabaseReference
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val cartList = mutableListOf<CardModel>()
                var subTotalPrice = 0.0
                for (item in querySnapshot) {
                    val cartProduct = item.toObject<CardModel>()
                    cartList.add(cartProduct)
                    subTotalPrice += cartProduct.price!!.toDouble()
                }
                val totalPrice = subTotalPrice + 15.0
                _cartItems.value = cartList
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    // Function to remove a cart item
    fun removeCartItem(item: CardModel) {
        // Implement the removal logic
    }
}
