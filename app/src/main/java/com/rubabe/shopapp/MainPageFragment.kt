package com.rubabe.shopapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.Extensions.toast
import com.rubabe.shopapp.adapter.BeautyDisplayAdapter
import com.rubabe.shopapp.adapter.CategoryOnClickInterface
import com.rubabe.shopapp.adapter.LikeOnClickInterface
import com.rubabe.shopapp.adapter.MainCategoryAdapter
import com.rubabe.shopapp.adapter.ProductOnClickInterface
import com.rubabe.shopapp.databinding.FragmentMainPageBinding
import com.rubabe.shopapp.model.BeautyDisplayModel
import com.rubabe.shopapp.model.LikeModel
import com.google.firebase.database.*


class MainPageFragment : Fragment(R.layout.fragment_main_page),
    CategoryOnClickInterface,
    ProductOnClickInterface, LikeOnClickInterface {


    private lateinit var binding: FragmentMainPageBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var productList: ArrayList<BeautyDisplayModel>
    private lateinit var categoryList: LinkedHashSet<String>
    private lateinit var productsAdapter: BeautyDisplayAdapter
    private lateinit var categoryAdapter: MainCategoryAdapter
    private lateinit var auth: FirebaseAuth
    private var likeDBRef = Firebase.firestore.collection("LikedProducts")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMainPageBinding.bind(view)
        categoryList = LinkedHashSet()
        productList = ArrayList()
        databaseReference = FirebaseDatabase.getInstance().getReference("products")
        auth = FirebaseAuth.getInstance()


        // region implements category Recycler view

        categoryList.add("Trending")
        binding.rvMainCategories.setHasFixedSize(true)
        val categoryLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvMainCategories.layoutManager = categoryLayoutManager
        categoryAdapter = MainCategoryAdapter(categoryList, this)
        binding.rvMainCategories.adapter = categoryAdapter
        setCategoryList()

        // endregion implements category Recycler view


        // region implements products Recycler view

        val productLayoutManager = GridLayoutManager(context, 2)
        productsAdapter = BeautyDisplayAdapter(requireContext(), productList, this, this)
        binding.rvMainProductList.layoutManager = productLayoutManager
        binding.rvMainProductList.adapter = productsAdapter
        setProductsData()
        // endregion implements products Recycler view


        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.switch_mainFragment_self)
                    true
                }

                R.id.likeFragment -> {
//                    requireActivity().toast("Like Page coming Soon")
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.switch_mainFragment_to_likeFragment)
                    true
                }

                R.id.cardFragment -> {

                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.switch_mainFragment_to_cardFragment)

                    true
                }

                R.id.profileFragment -> {

                    auth.signOut()
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.switch_mainFragment_to_signInFragmentFragment)
                    true
                }

                else -> false

            }

        }


    }

    private fun setCategoryList() {

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                categoryList.clear()
                categoryList.add("Trending")

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)
                        //val imageUrl = products?.imageUrl

                        categoryList.add(products!!.brand!!)


                    }

                    categoryAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }


        databaseReference.addValueEventListener(valueEvent)


    }


    private fun setProductsData() {

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)
                        productList.add(products!!)
                    }

                    productsAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }

        databaseReference.addValueEventListener(valueEvent)

    }

    override fun onClickCategory(button: Button) {
        binding.tvMainCategories.text = button.text

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)

                        if (products!!.brand == button.text) {
                            productList.add(products)
                        }

                        if (button.text == "Trending") {
                            productList.add(products)
                        }

                    }

                    productsAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }

        databaseReference.addValueEventListener(valueEvent)

    }


    override fun onClickProduct(item: BeautyDisplayModel) {

        val direction = MainPageFragmentDirections.switchMainFragmentToDetailsFragment(item.id!!)


        Navigation.findNavController(requireView())
            .navigate(direction)

    }

    override fun onClickLike(item: BeautyDisplayModel) {

        likeDBRef
            .add(
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
                requireActivity().toast("Added to Liked Items")
            }
            .addOnFailureListener {
                requireActivity().toast("Failed to Add to Liked")
            }

    }


}