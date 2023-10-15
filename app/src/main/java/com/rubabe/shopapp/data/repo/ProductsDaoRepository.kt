package com.rubabe.shopapp.data.repo


import android.app.Activity
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.data.model.BeautyDisplayModel
import com.rubabe.shopapp.data.model.LikeModel
import com.rubabe.shopapp.ui.adapter.CategoryOnClickInterface
import com.rubabe.shopapp.ui.adapter.LikeOnClickInterface


class ProductsDaoRepository(var refProducts: DatabaseReference) : LikeOnClickInterface,
    CategoryOnClickInterface {
    private var categoryList: MutableLiveData<LinkedHashSet<BeautyDisplayModel>> = MutableLiveData()
    private lateinit var auth: FirebaseAuth
    private var likeDBRef = Firebase.firestore.collection("LikedProducts")
    private var productList: MutableLiveData<ArrayList<BeautyDisplayModel>> = MutableLiveData()


    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("products")


    override fun onClickLike(item: BeautyDisplayModel) {
        auth = FirebaseAuth.getInstance()
        likeDBRef.add(
            LikeModel(
                item.id,
                auth.currentUser!!.uid,
                item.brand,
                item.description,
                item.imageUrl,
                item.name,
                item.price
            )

        )

            .addOnSuccessListener {

                println("Added to Liked Items")


            }
            .addOnFailureListener {
                println("Failed to Add to Liked")
            }
    }


    fun setList() {
        val newCategoryList = LinkedHashSet<BeautyDisplayModel>()
        val newProductList = ArrayList<BeautyDisplayModel>()

        val trendingCategory = BeautyDisplayModel()
        trendingCategory.brand = "Trending"
        newCategoryList.add(trendingCategory)
        refProducts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)
                        newCategoryList.add(products!!)
                        newProductList.add(products)

                    }
                    categoryList.value = newCategoryList
                    productList.value = newProductList
                }

            }

            override fun onCancelled(error: DatabaseError) {
                println("Error")
            }

        })


    }

    fun getCategory(): MutableLiveData<LinkedHashSet<BeautyDisplayModel>> {
        println("categoryList: $categoryList")
        return categoryList
    }

    override fun onClickCategory(button: Button) {
        val newProductList = ArrayList<BeautyDisplayModel>()
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newProductList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)

                        if (products!!.brand == button.text) {
                            newProductList.add(products)
                        }

                        if (button.text == "Trending") {
                            newProductList.add(products)
                        }
                    }

                    productList.postValue(newProductList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error")
            }
        }

        databaseReference.addValueEventListener(valueEvent)
    }


    fun getProduct(): MutableLiveData<ArrayList<BeautyDisplayModel>> {
        println("productList: $productList")
        return productList
    }

}