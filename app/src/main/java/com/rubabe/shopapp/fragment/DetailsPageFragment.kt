package com.rubabe.shopapp.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.R
import com.rubabe.shopapp.adapter.SizeAdapter
import com.rubabe.shopapp.adapter.SizeOnClickInterface
import com.rubabe.shopapp.databinding.FragmentDetailsPageBinding
import com.rubabe.shopapp.model.BeautyDisplayModel
import com.rubabe.shopapp.model.ProductOrderModel
import com.rubabe.shopapp.utils.Extensions.toast


class DetailsPageFragment : Fragment(R.layout.fragment_details_page), SizeOnClickInterface {

    private lateinit var binding: FragmentDetailsPageBinding
    private lateinit var productDatabaseReference: DatabaseReference
    private lateinit var sizeAdapter: SizeAdapter
    private lateinit var auth: FirebaseAuth
    private val args: DetailsPageFragmentArgs by navArgs()

    private val orderDatabaseReference = Firebase.firestore.collection("orders")

    private lateinit var currentUID: String
    private lateinit var orderImageUrl: String
    private lateinit var orderName: String
    private var orderSize: String? = null
    private var orderQuantity: Int = 1
    private lateinit var orderPrice: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsPageBinding.bind(view)

        productDatabaseReference = FirebaseDatabase.getInstance().getReference("products")

        val productId = args.productId
        val switchId = args.switchId


        auth = FirebaseAuth.getInstance()

        currentUID = auth.currentUser!!.uid

        binding.detailActualToolbar.setNavigationOnClickListener {
            //Navigation.findNavController(requireView()).popBackStack()

            if (switchId == 0) {
                findNavController().navigate(R.id.action_detailsFragment_to_likePageFragment)
            } else {
                findNavController().navigate(R.id.action_detailsFragment_to_mainPageFragment)
            }
        }


        // region implements firebase product display
        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(BeautyDisplayModel::class.java)

                        if (products!!.id == productId) {
                            Glide
                                .with(requireContext())
                                .load(products.imageUrl)
                                .into(binding.ivDetails)

                            orderImageUrl = products.imageUrl!!
                            orderName = products.name!!
                            orderPrice = products.price!!

                            binding.tvDetailsProductPrice.text = "$${products.price}"
                            binding.tvDetailsProductName.text = "${products.brand} ${products.name}"
                            binding.tvDetailsProductDescription.text = products.description
                        }


                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {
                requireActivity().toast(error.message)
            }

        }


        productDatabaseReference.addValueEventListener(valueEvent)


        val sizeList = ArrayList<String>()
        sizeList.add("All")
        sizeList.add("Light")
        sizeList.add("Medium")
        sizeList.add("Deep")

        sizeAdapter = SizeAdapter(requireContext(), sizeList, this)
        binding.rvSelectSize.adapter = sizeAdapter

        println("switchId: $switchId")
        binding.btnDetailsAddToCart.setOnClickListener {

            // TODO: Add Data to FireBase FireStore Database

            val orderedProduct = ProductOrderModel(
                currentUID,
                productId,
                orderImageUrl,
                orderName,
                orderSize,
                orderQuantity,
                orderPrice
            )

            if (orderSize.isNullOrBlank()) {
                requireActivity().toast("Select Type")
            } else {
                addDataToOrdersDatabase(orderedProduct)

                val direction =
                    DetailsPageFragmentDirections.switchfromDetailsFragmentToCardFargment(switchId)

                Navigation.findNavController(requireView())
                    .navigate(direction)
            }


        }

    }

    private fun addDataToOrdersDatabase(orderedProduct: ProductOrderModel) {

        /*orderDatabaseReference.add(orderedProduct).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                requireActivity().toast("Product added to the basket")
            } else {
                requireActivity().toast(task.exception!!.localizedMessage!!)
            }
        }*/

        orderDatabaseReference.whereEqualTo("uid", orderedProduct.uid)
            .whereEqualTo("pid", orderedProduct.pid).get()
            .addOnSuccessListener { querySnapshot ->
                println("is empty? " + querySnapshot.isEmpty)
                println("doc size: " + querySnapshot.documents.size)
                if (querySnapshot != null) {
                    if (querySnapshot.documents.isNotEmpty()) {
                        val document = querySnapshot.documents[0]
                        val productOrderModel: ProductOrderModel? =
                            document.toObject(ProductOrderModel::class.java)

                        if (productOrderModel != null) {
                            productOrderModel.quantity = productOrderModel.quantity?.plus(1)
                            orderDatabaseReference.document(document.id).set(productOrderModel)
                                .addOnSuccessListener {
                                    println("updated product")
                                }
                                .addOnFailureListener { exception ->
                                    println(exception)
                                }
                        }
                    } else {
                        orderDatabaseReference.add(orderedProduct).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                requireActivity().toast("Product added to the basket")
                            } else {
                                requireActivity().toast(task.exception!!.localizedMessage!!)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "There is not enough produce", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onClickSize(button: Button, position: Int) {
        orderSize = button.text.toString()
        requireActivity().toast("${button.text} Selected")
    }

}