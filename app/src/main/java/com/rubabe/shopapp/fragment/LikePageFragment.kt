package com.rubabe.shopapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.utils.Extensions.toast
import com.rubabe.shopapp.R
import com.rubabe.shopapp.adapter.LikeAdapter
import com.rubabe.shopapp.adapter.LikedOnClickInterface
import com.rubabe.shopapp.adapter.LikedProductOnClickInterface
import com.rubabe.shopapp.databinding.FragmentLikePageBinding
import com.rubabe.shopapp.model.LikeModel

class LikePageFragment(): Fragment(R.layout.fragment_like_page), LikedProductOnClickInterface,
    LikedOnClickInterface {

    private lateinit var binding: FragmentLikePageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: LikeAdapter
    private lateinit var likedProductList: ArrayList<LikeModel>


    private var likeDBRef = Firebase.firestore.collection("LikedProducts")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLikePageBinding.bind(view)
        auth = FirebaseAuth.getInstance()
        likedProductList = ArrayList()

        adapter = LikeAdapter(requireContext(),likedProductList,this,this)

        binding.likeActualToolbar.setNavigationOnClickListener {
          findNavController().navigate(R.id.action_likePageFragment_to_mainPageFragment)
        }


        val productLayoutManager = GridLayoutManager(context, 2)
        binding.rvLikedProducts.layoutManager = productLayoutManager
        binding.rvLikedProducts.adapter = adapter


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
                if(likedProductList.size == 0){
                    binding.sleepImage.visibility = View.VISIBLE
                    binding.noItem.visibility = View.VISIBLE
                }


                // Check if the likedProductList is empty
                /*if (likedProductList.isEmpty()) {
                    // Show a toast message indicating that there are no liked items
                    findNavController().navigate(R.id.action_likePageFragment_to_noItemFragment)
                    //requireActivity().toast("No liked items yet")
                }*/

            }
            .addOnFailureListener {
                requireActivity().toast(it.localizedMessage!!)
            }
    }


    override fun onClickProduct(item: LikeModel) {

        //Bu iki setir sonradan yazdim
        val direction = LikePageFragmentDirections.actionLikePageFragmentToDetailsFragment(item.pid!!)

        Navigation.findNavController(requireView())
            .navigate(direction)

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
                    if(likedProductList.size == 0){
                        binding.sleepImage.visibility = View.VISIBLE
                        binding.noItem.visibility = View.VISIBLE
                    }

                 /*   if (likedProductList.isEmpty()) {
                        // Show a toast message indicating that there are no liked items
                        findNavController().navigate(R.id.action_likePageFragment_to_noItemFragment)
                        //requireActivity().toast("No liked items yet")
                    } else {
                        requireActivity().toast("Removed From the Liked Items")
                    }*/
                }
            }
            .addOnFailureListener {
                requireActivity().toast("Failed To Remove From Liked Items")
            }
    }


}