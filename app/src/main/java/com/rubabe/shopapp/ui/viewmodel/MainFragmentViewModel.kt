package com.rubabe.shopapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.rubabe.shopapp.data.model.BeautyDisplayModel

class MainFragmentViewModel : ViewModel() {

    private var productList = MutableLiveData<ArrayList<BeautyDisplayModel>>()
    private var categoryList = MutableLiveData<LinkedHashSet<BeautyDisplayModel>>()
    private val errorMessage = MutableLiveData<String>()
    private lateinit var databaseReference: DatabaseReference

    fun observeProduct(): MutableLiveData<ArrayList<BeautyDisplayModel>> {
        return productList
    }

    fun observeCategory(): MutableLiveData<LinkedHashSet<BeautyDisplayModel>> {
        return categoryList
    }

    fun observeErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun setDatabaseReference(ref: DatabaseReference) {
        databaseReference = ref
    }


    fun setCategoryList() {
        val newCategoryList = LinkedHashSet<BeautyDisplayModel>()

        // Create a BeautyDisplayModel instance for the "Trending" category
        val trendingCategory = BeautyDisplayModel()
        trendingCategory.brand = "Trending"
        newCategoryList.add(trendingCategory)
       // newCategoryList.add("Trending")

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)
                        newCategoryList.add(products!!)
                    }
                }

                categoryList.postValue(newCategoryList)
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage.value = error.message // Set the error message
            }
        }
        databaseReference.addValueEventListener(valueEvent)
    }



    fun setProductsData() {
        val newProductList = ArrayList<BeautyDisplayModel>()
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                newProductList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)
                        newProductList.add(products!!)
                    }
                    productList.postValue(newProductList)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage.value = error.message
            }

        }

        databaseReference.addValueEventListener(valueEvent)

    }

}