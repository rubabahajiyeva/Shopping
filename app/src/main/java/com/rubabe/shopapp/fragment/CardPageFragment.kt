package com.rubabe.shopapp.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.adapter.CardAdapter
import com.rubabe.shopapp.databinding.FragmentCardPageBinding
import com.rubabe.shopapp.model.CardModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.rubabe.shopapp.utils.Extensions.toast
import com.rubabe.shopapp.R


class CardPageFragment : Fragment(R.layout.fragment_card_page), CardAdapter.OnItemClickListener,
    CardAdapter.OnQuantityChangeListener {

    private lateinit var binding: FragmentCardPageBinding
    private lateinit var cartList: ArrayList<CardModel>
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: CardAdapter
    private var subTotalPrice = 0.0
    private var totalPrice = 0.0
    private var deliveryPrice = 15.0
    private var count = 1.0
    private var orderDatabaseReference = Firebase.firestore.collection("orders")

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCardPageBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.cardActualToolbar.setOnClickListener {

            findNavController().navigate(R.id.action_cardPageFragment_to_mainPageFragment)


           /* findNavController().popBackStack(R.id.detailsFragment, false)

            findNavController().navigateUp()*/
        }


        val layoutManager = LinearLayoutManager(context)


        cartList = ArrayList()

        retrieveCartItems()


        adapter = CardAdapter(requireContext(), cartList, this, this)
        binding.rvCartItems.adapter = adapter
        binding.rvCartItems.layoutManager = layoutManager

        binding.deliveryPriceTV.text = "$$deliveryPrice"

        binding.btnCartCheckout.setOnClickListener {

            cartList.clear()

            if (binding.tvLastTotalPrice.text.toString() == "$0") {
                binding.tvLastTotalPrice.text = "Min 1 product is Required"
            } else {

                requireActivity().toast("You've Ordered Products worth ${totalPrice}\n Your Product will be delivered in next 7 days")
                /* binding.tvLastTotalPrice.text= "$0"
                 binding.tvLastSubTotalPrice.text = "$0"
                 binding.deliveryPriceTV.text = "$0"*/
            }

            binding.tvLastTotalPrice.setTextColor(Color.RED)
            adapter.notifyDataSetChanged()
        }


    }

    private fun retrieveCartItems(): Double {

        orderDatabaseReference
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (item in querySnapshot) {
                    val cartProduct = item.toObject<CardModel>()

                    val totalCount = onQuantityChanged(cartList.size)
                    cartList.add(cartProduct)
                    subTotalPrice += cartProduct.price!!.toDouble()
                    println("count sub: $count")
                    println("cartProduct.price: ${cartProduct.price}")
                    val sub = "$${subTotalPrice}"
                    if (cartList.size == 1) {
                        totalPrice += cartProduct.price.toDouble() + deliveryPrice
                    } else {
                        totalPrice += cartProduct.price.toDouble()
                    }

                    val total = "$${totalPrice}"
                    binding.tvLastSubTotalPrice.text = sub
                    binding.tvLastTotalPrice.text = total
                    binding.tvLastSubTotalItems.text = "SubTotal Items(${cartList.size})"
                    adapter.notifyDataSetChanged()


                }

            }
            .addOnFailureListener {
                requireActivity().toast(it.localizedMessage!!)
            }

        return subTotalPrice
    }

    override fun onItemClick(item: CardModel, position: Int) {
        val uid = item.uid
        val pid = item.pid
        val size = item.size

        orderDatabaseReference
            .whereEqualTo("uid", uid)
            .whereEqualTo("pid", pid)
            .whereEqualTo("size", size)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (docSnapshot in querySnapshot) {
                    val docId = docSnapshot.id
                    orderDatabaseReference.document(docId).delete()
                }

                val itemIndex = cartList.indexOf(item)
                if (itemIndex != -1) {
                    cartList.removeAt(itemIndex)
                    adapter.notifyItemRemoved(itemIndex)
                    updateTotalPrice()
                    requireActivity().toast("Removed Successfully!")
                } else {
                    requireActivity().toast("Item not found in the cart.")
                }
            }
            .addOnFailureListener {
                requireActivity().toast("Failed to remove")
            }
    }


    override fun onQuantityChanged(newCount: Int): Double {
        count = newCount.toDouble() // Update the count value in the fragment
        println("count: $count")

        updateTotalPrice()
        return count
    }

    private fun updateTotalPrice() {
        println("totalPrice: $totalPrice")
        println("subtotalPrice: $subTotalPrice")
        val total = "$${(subTotalPrice * count) + deliveryPrice}" // Calculate the new total price
        println("count first: $count")
        //   binding.tvLastSubTotalItems.text = "SubTotal Items(${count.toInt()})"
        println("count second: $count")
        binding.tvLastTotalPrice.text = total // Update the UI
        println("total: $total")


        println("cartList: $cartList")

    }


}