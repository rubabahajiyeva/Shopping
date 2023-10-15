package com.rubabe.shopapp.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.R
import com.rubabe.shopapp.ui.adapter.CardAdapter
import com.rubabe.shopapp.databinding.FragmentCardPageBinding
import com.rubabe.shopapp.data.model.CardModel
import com.rubabe.shopapp.utils.Extensions.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardPageFragment : Fragment(), CardAdapter.OnItemClickListener,
    CardAdapter.OnQuantityChangeListener, CardAdapter.OnNavigationClickListener {

    private lateinit var binding: FragmentCardPageBinding
    //private lateinit var viewModel: CardViewModel
    private lateinit var cartList: ArrayList<CardModel>
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: CardAdapter
    private var subTotalPrice = 0.0
    private var totalPrice = 0.0
    private var deliveryPrice = 15.0
    private var count = 1.0



    private var orderDatabaseReference = Firebase.firestore.collection("orders")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_card_page, container, false)


        binding.cardFragment = this
        binding.cardPageToolbarHeader = "My Basket"
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      /*  val tempViewModel: CardViewModel by viewModels()
        viewModel = tempViewModel*/
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()


        cartList = ArrayList()

        retrieveCartItems()


        adapter = CardAdapter(requireContext(), cartList, this, this, binding.btnCartCheckout, this)
        binding.cardAdapter = adapter


    }

    @SuppressLint("SetTextI18n")
    private fun retrieveCartItems(): Double {

        orderDatabaseReference
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isEmpty()) {
                    binding.tvLastTotalPrice.text = "$0.0"
                    binding.tvLastSubTotalPrice.text = "$0.0"
                } else {

                    for (item in querySnapshot) {
                        val cartProduct = item.toObject<CardModel>()
                        cartList.add(cartProduct)
                        binding.tvLastSubTotalItems.text = "SubTotal Items(${cartList.size})"
                        adapter.notifyDataSetChanged()


                    }
                    println("it works cartList")
                    println(cartList.size)
                    println(cartList)
                    var subTotal = 0.0

                    for (cartItem in cartList) {
                        subTotal += (cartItem.quantity!! * cartItem.price!!.toDouble())
                    }

                    subTotalPrice = subTotal
                    totalPrice = deliveryPrice + subTotal
                    updateTotalPrice()
                }
            }
            .addOnFailureListener {
                requireActivity().toast(it.localizedMessage!!)
            }



        return subTotalPrice
    }


    @SuppressLint("SetTextI18n")
    override fun onItemClick(item: CardModel) {
        if (isAdded) {
            val uid = item.uid
            val pid = item.pid
            val type = item.type

            orderDatabaseReference
                .whereEqualTo("uid", uid)
                .whereEqualTo("pid", pid)
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.documents.isEmpty()) {
                        binding.tvLastTotalPrice.text = "$0.0"
                        binding.tvLastSubTotalPrice.text = "$0.0"
                    } else {


                        for (item in querySnapshot) {
                            orderDatabaseReference.document(item.id).delete()
                            cartList.remove(item.toObject<CardModel>())
                            adapter.notifyDataSetChanged()
                        }

                        val itemIndex = cartList.indexOf(item)
                        if (itemIndex != -1) {
                            cartList.removeAt(itemIndex)
                            adapter.notifyItemRemoved(itemIndex)
                            updateTotalPrice()
                            requireActivity().toast("Item not found in the cart")
                        }
                    }
                }
                .addOnFailureListener {
                    requireActivity().toast("Failed to remove")
                }

        }
    }


    override fun onQuantityChanged(newCount: Int): Double {
        count = newCount.toDouble() // Update the count value in the fragment
        println("count: $count")

        updateTotalPrice()
        return count
    }

    private fun updateTotalPrice() {
        binding.tvLastSubTotalPrice.text = "$$subTotalPrice"
        binding.tvLastTotalPrice.text = "$$totalPrice"
    }

    override fun onNavigateToSuccessFragment() {
        binding.tvLastTotalPrice.text = 0.0.toString()
        binding.tvLastSubTotalPrice.text = 0.0.toString()
        findNavController().navigate(R.id.action_cardPageFragment_to_successfulOrderFragment)
    }

    fun navigate() {
        val bundle: CardPageFragmentArgs by navArgs()
        var switchId = bundle.switchId

        if (switchId == 0 || switchId == 1) {
            findNavController().navigate(R.id.action_cardFragment_to_detailsFragment)
        }
    }

    fun checkout() {
        cartList.clear()

        if (binding.tvLastTotalPrice.text.toString() == "$0") {
            binding.tvLastTotalPrice.text = "Min 1 product is Required"
        }
        binding.tvLastTotalPrice.setTextColor(Color.RED)
        adapter.notifyDataSetChanged()

    }


}