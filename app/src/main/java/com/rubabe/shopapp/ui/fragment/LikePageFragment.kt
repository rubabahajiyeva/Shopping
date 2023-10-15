package com.rubabe.shopapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.ui.viewmodel.LikedItemsViewModel
import com.rubabe.shopapp.utils.Extensions.toast
import com.rubabe.shopapp.R
import com.rubabe.shopapp.ui.adapter.LikeAdapter
import com.rubabe.shopapp.ui.adapter.LikedOnClickInterface
import com.rubabe.shopapp.ui.adapter.LikedProductOnClickInterface
import com.rubabe.shopapp.databinding.FragmentLikePageBinding
import com.rubabe.shopapp.data.model.LikeModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikePageFragment() : Fragment(), LikedProductOnClickInterface,
    LikedOnClickInterface {
    private lateinit var likedItemsViewModel: LikedItemsViewModel

    private lateinit var binding: FragmentLikePageBinding
    private lateinit var viewModel: LikedItemsViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: LikeAdapter
    private lateinit var likedProductList: LinkedHashSet<LikeModel>


    private var likeDBRef = Firebase.firestore.collection("LikedProducts")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_like_page, container, false)

        binding.likePageFragment = this
        binding.likeToolbarHeader = "My Favorites"

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: LikedItemsViewModel by viewModels()
        viewModel = tempViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        likedItemsViewModel = ViewModelProvider(requireActivity())[LikedItemsViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        likedProductList = LinkedHashSet()



        adapter = LikeAdapter(requireContext(), likedProductList, this, this, likedItemsViewModel)
        binding.likeAdapter = adapter

        displayLikedProducts()

    }


    @SuppressLint("SuspiciousIndentation")
    private fun displayLikedProducts() {

        likeDBRef
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                likedProductList.clear() // Clear the list before adding new items
                for (item in querySnapshot) {
                    val likedProduct = item.toObject<LikeModel>()
                    likedProductList.add(likedProduct)


                }

                adapter.notifyDataSetChanged()
                if (likedProductList.size == 0) {
                    binding.sleepImage.visibility = View.VISIBLE
                    binding.noItem.visibility = View.VISIBLE
                }


            }
            .addOnFailureListener {
                requireActivity().toast(it.localizedMessage!!)
            }
    }


    override fun onClickProduct(item: LikeModel) {

        findNavController().navigate(
            R.id.action_likePageFragment_to_detailsFragment,
            bundleOf("productId" to item.pid, "switchId" to 0), NavOptions.Builder()
                .setPopUpTo(
                    R.id.likeFragment,
                    true
                ).build()
        )


    }

    override fun onClickLike(item: LikeModel) {

        likeDBRef
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .whereEqualTo("pid", item.pid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (item in querySnapshot) {
                    likeDBRef.document(item.id).delete()
                    likedProductList.remove(item.toObject<LikeModel>())
                    adapter.notifyDataSetChanged()
                    if (likedProductList.size == 0) {
                        binding.sleepImage.visibility = View.VISIBLE
                        binding.noItem.visibility = View.VISIBLE
                    }
                }
            }
            .addOnFailureListener {
                requireActivity().toast("Failed To Remove From Liked Items")
            }

    }

}