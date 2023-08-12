package com.rubabe.shopapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.adapter.CardAdapter
import com.rubabe.shopapp.databinding.FragmentCardPageBinding
import com.rubabe.shopapp.model.CardModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.rubabe.shopapp.Extensions.toast

class CardPageFragment : Fragment(R.layout.fragment_card_page), CardAdapter.OnLongClickRemove {

    private lateinit var binding: FragmentCardPageBinding
    private lateinit var cartList: ArrayList<CardModel>
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: CardAdapter
    private var subTotalPrice = 0.0
    private var totalPrice = 0.0
    private var deliveryPrice = 15

    private var orderDatabaseReference = Firebase.firestore.collection("orders")

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCardPageBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.cardActualToolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }


        val layoutManager = LinearLayoutManager(context)


        cartList = ArrayList()

        retrieveCartItems()



        adapter = CardAdapter(requireContext(),cartList ,this)
        binding.rvCartItems.adapter = adapter
        binding.rvCartItems.layoutManager = layoutManager

        binding.deliveryPriceTV.text = "$$deliveryPrice"

        binding.btnCartCheckout.setOnClickListener {

            cartList.clear()

            if(binding.tvLastTotalPrice.text.toString() == "$0"){
                binding.tvLastTotalPrice.text ="Min 1 product is Required"
            }else{
                requireActivity().toast("You've Ordered Products worth ${totalPrice}\n Your Product will be delivered in next 7 days")
            }

            binding.tvLastTotalPrice.setTextColor(Color.RED)
            adapter.notifyDataSetChanged()
        }


    }

    private fun retrieveCartItems() {

        orderDatabaseReference
            .whereEqualTo("uid",auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (item in querySnapshot) {
                    val cartProduct = item.toObject<CardModel>()


                    cartList.add(cartProduct)
                    subTotalPrice += cartProduct.price!!.toDouble()
                    var sub = "$${subTotalPrice}"
                    totalPrice += cartProduct.price.toDouble() + deliveryPrice
                    var total = "$${totalPrice}"
                    binding.tvLastSubTotalprice.text = sub
                    binding.tvLastTotalPrice.text = total
                    binding.tvLastSubTotalItems.text = "SubTotal Items(${cartList.size})"
                    adapter.notifyDataSetChanged()


                }

            }
            .addOnFailureListener{
                requireActivity().toast(it.localizedMessage!!)
            }


    }

    override fun onLongRemove(item: CardModel , position:Int) {


        orderDatabaseReference
            .whereEqualTo("uid",item.uid)
            .whereEqualTo("pid",item.pid)
            .whereEqualTo("size",item.size)
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (item in querySnapshot){
                    orderDatabaseReference.document(item.id).delete()
                    cartList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    requireActivity().toast("Removed Successfully!!!")
                }

            }
            .addOnFailureListener {
                requireActivity().toast("Failed to remove")
            }

    }


}