package com.rubabe.shopapp.ui.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.ui.viewmodel.LikedItemsViewModel
import com.rubabe.shopapp.R
import com.rubabe.shopapp.data.model.BeautyDisplayModel
import com.rubabe.shopapp.databinding.FragmentHomeBinding
import com.rubabe.shopapp.ui.adapter.BeautyDisplayAdapter
import com.rubabe.shopapp.ui.adapter.CategoryOnClickInterface
import com.rubabe.shopapp.ui.adapter.LikeOnClickInterface
import com.rubabe.shopapp.ui.adapter.MainCategoryAdapter
import com.rubabe.shopapp.ui.adapter.ProductOnClickInterface
import com.rubabe.shopapp.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(),
    CategoryOnClickInterface,
    ProductOnClickInterface, LikeOnClickInterface {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var productList: ArrayList<BeautyDisplayModel>

    private lateinit var categoryList: LinkedHashSet<BeautyDisplayModel>
    private lateinit var productsAdapter: BeautyDisplayAdapter
    private lateinit var categoryAdapter: MainCategoryAdapter
    private lateinit var auth: FirebaseAuth


    private lateinit var viewModel: HomeViewModel
    private lateinit var likedItemsViewModel: LikedItemsViewModel


    private var likeDBRef = Firebase.firestore.collection("LikedProducts")

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        categoryList = LinkedHashSet()
        productList = ArrayList()
        binding.homeFragment = this


        likedItemsViewModel = ViewModelProvider(requireActivity())[LikedItemsViewModel::class.java]
        categoryAdapter = MainCategoryAdapter(requireContext(), categoryList, this)
        productsAdapter = BeautyDisplayAdapter(requireContext(), productList, this, this, likedItemsViewModel)

        binding.categoryAdapter = categoryAdapter
        binding.beautyDisplayAdapter = productsAdapter


        viewModel.categoryList.observe(viewLifecycleOwner) { category ->
            this.categoryList = category
            categoryAdapter.list = category
            categoryAdapter.notifyDataSetChanged()
        }



        viewModel.productList.observe(viewLifecycleOwner){ product ->
            this.productList = product
            productsAdapter.list = product
            productsAdapter.notifyDataSetChanged()
        }
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomeViewModel by viewModels()
        viewModel = tempViewModel
    }


    override fun onClickCategory(button: Button) {
        binding.tvMainCategories.text = button.text
        viewModel.onClickCategory(button)

    }


    override fun onClickProduct(item: BeautyDisplayModel) {

        /*Navigation.navigate(R.id.switch_mainFragment_to_detailsFragment,  bundleOf("productId" to item.id!!, "switchId" to 1), NavOptions.Builder()
            .setPopUpTo(
                R.id.homeFragment,
                true
            ).build())*/
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

        viewModel.click(item)


    }


    fun seeAll() {

        binding.tvMainCategories.text = "All Products"

         viewModel.addCategory()

    }


}