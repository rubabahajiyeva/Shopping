package com.rubabe.shopapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.rubabe.shopapp.model.CardModel

class CardViewModel : ViewModel() {

    private val _cartList = MutableLiveData<List<CardModel>>()
    val cartList: LiveData<List<CardModel>> = _cartList

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private val orderDatabaseReference = FirebaseFirestore.getInstance().collection("orders")

    init {
        _cartList.value = ArrayList()
        _totalPrice.value = 0.0
    }

    fun retrieveCartItems(userId: String) {
        orderDatabaseReference
            .whereEqualTo("uid", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val cartList = mutableListOf<CardModel>()
                var subTotalPrice = 0.0

                for (item in querySnapshot) {
                    val cartProduct = item.toObject(CardModel::class.java)
                    cartList.add(cartProduct)
                    subTotalPrice += cartProduct.price!!.toDouble()
                }

                val totalPrice = subTotalPrice + 15.0 // Assuming deliveryPrice is 15.0
                _cartList.value = cartList
                _totalPrice.value = totalPrice
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    fun removeFromCart(item: CardModel) {
        orderDatabaseReference
            .whereEqualTo("uid", item.uid)
            .whereEqualTo("pid", item.pid)
            .whereEqualTo("size", item.size)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (docSnapshot in querySnapshot) {
                    orderDatabaseReference.document(docSnapshot.id).delete()
                }
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
}
