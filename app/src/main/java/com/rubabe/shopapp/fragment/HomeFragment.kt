package com.rubabe.shopapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.LikedItemsViewModel
import com.rubabe.shopapp.R
import com.rubabe.shopapp.adapter.BeautyDisplayAdapter
import com.rubabe.shopapp.adapter.CategoryOnClickInterface
import com.rubabe.shopapp.adapter.LikeOnClickInterface
import com.rubabe.shopapp.adapter.MainCategoryAdapter
import com.rubabe.shopapp.adapter.ProductOnClickInterface
import com.rubabe.shopapp.databinding.FragmentHomeBinding
import com.rubabe.shopapp.model.BeautyDisplayModel
import com.rubabe.shopapp.model.LikeModel
import com.rubabe.shopapp.utils.Extensions.toast
import com.rubabe.shopapp.viewmodel.MainFragmentViewModel


class HomeFragment : Fragment(R.layout.fragment_home),
    CategoryOnClickInterface,
    ProductOnClickInterface, LikeOnClickInterface {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var productList: ArrayList<BeautyDisplayModel>
    private lateinit var categoryList: LinkedHashSet<String>
    private lateinit var productsAdapter: BeautyDisplayAdapter
    private lateinit var categoryAdapter: MainCategoryAdapter
    private lateinit var auth: FirebaseAuth

    private lateinit var viewModel: MainFragmentViewModel
    private lateinit var likedItemsViewModel: LikedItemsViewModel


    private var likeDBRef = Firebase.firestore.collection("LikedProducts")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        likedItemsViewModel = ViewModelProvider(requireActivity())[LikedItemsViewModel::class.java]

        binding = FragmentHomeBinding.bind(view)
        categoryList = LinkedHashSet()
        productList = ArrayList()
        databaseReference = FirebaseDatabase.getInstance().getReference("products")
        auth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]


        binding.rvMainCategories.setHasFixedSize(true)
        val categoryLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvMainCategories.layoutManager = categoryLayoutManager
        categoryAdapter = MainCategoryAdapter(requireContext(), categoryList, this)
        binding.rvMainCategories.adapter = categoryAdapter
        viewModel.setDatabaseReference(databaseReference)
        viewModel.setCategoryList()
        setUpObservers()


        val productLayoutManager = GridLayoutManager(context, 2)
        productsAdapter =
            BeautyDisplayAdapter(requireContext(), productList, this, this, likedItemsViewModel)
        binding.rvMainProductList.layoutManager = productLayoutManager
        binding.rvMainProductList.adapter = productsAdapter
        viewModel.setProductsData()




        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.switch_mainFragment_self)
                    true
                }

                R.id.likeFragment -> {
                    //requireActivity().toast("Like Page coming Soon")

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
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.switch_mainFragment_to_profile_Fragment)
                    true
                }

                else -> false

            }

        }

        binding.tvSeeAll.setOnClickListener {
            // If the "See All" button is clicked, show all products
            binding.tvMainCategories.text = "All Products"
            viewModel.setProductsData()

        }

        // Register the onBackPressedCallback
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

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


        findNavController().navigate(
            R.id.switch_mainFragment_to_detailsFragment,
            bundleOf("productId" to item.id!!, "switchId" to 1), NavOptions.Builder()
                .setPopUpTo(
                    R.id.homeFragment,
                    true
                ).build()
        )


    }

    override fun onClickLike(item: BeautyDisplayModel) {

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

                requireActivity().toast("Added to Liked Items")


            }
            .addOnFailureListener {
                requireActivity().toast("Failed to Add to Liked")
            }


    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle back button press
            // Remove fragments from the back stack
            while (findNavController().popBackStack()) {
                // Keep popping the back stack until it's empty
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Unregister the onBackPressedCallback
        onBackPressedCallback.remove()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpObservers() {
        viewModel.observeCategory().observe(viewLifecycleOwner, Observer { category ->
            this.categoryList = category
            (binding.rvMainCategories.adapter as MainCategoryAdapter).list = category
            (binding.rvMainCategories.adapter as MainCategoryAdapter).notifyDataSetChanged()

        })

        viewModel.observeProduct().observe(viewLifecycleOwner, Observer { product ->
            this.productList = product
            (binding.rvMainProductList.adapter as BeautyDisplayAdapter).list = product
            (binding.rvMainProductList.adapter as BeautyDisplayAdapter).notifyDataSetChanged()
        })

        viewModel.observeErrorMessage().observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }


    }

}