package com.rubabe.shopapp.adapter

import android.annotation.SuppressLint
import com.rubabe.shopapp.databinding.CardProductItemBinding
import com.rubabe.shopapp.model.CardModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rubabe.shopapp.fragment.CardPageFragment
import com.rubabe.shopapp.model.ProductOrderModel
import com.rubabe.shopapp.utils.Delete
import com.rubabe.shopapp.utils.Extensions.toast


class CardAdapter(
    private val context: Context,
    private val list: ArrayList<CardModel>,
    private val onItemClickRemove: OnItemClickListener,
    private val onQuantityChangeListener: OnQuantityChangeListener,
    var button: AppCompatButton,
    private val onNavigationClickListener: OnNavigationClickListener
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    var count = 1
    private val orderDatabaseReference = Firebase.firestore.collection("orders")


    inner class ViewHolder(val binding: CardProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]

        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivCartProduct)


        holder.binding.tvCartProductName.text = currentItem.name
        holder.binding.tvCartProductPrice.text = "$${currentItem.price}"
        holder.binding.tvCartItemCount.text = currentItem.quantity.toString()
        holder.binding.tvCartProductSize.text = currentItem.size

        count = holder.binding.tvCartItemCount.text.toString().toInt()

        holder.binding.btnCartItemAdd.setOnClickListener {
            count++
            holder.binding.tvCartItemCount.text = count.toString()

            onQuantityChangeListener.onQuantityChanged(count)

            orderDatabaseReference.whereEqualTo("uid", currentItem.uid)
                .whereEqualTo("pid", currentItem.pid).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val productOrderModel: ProductOrderModel? =
                            document.toObject(ProductOrderModel::class.java)

                        if (productOrderModel != null) {
                            productOrderModel.quantity = count
                            orderDatabaseReference.document(document.id).set(productOrderModel)
                                .addOnSuccessListener {
                                    println("updated product")
                                }
                                .addOnFailureListener { exception ->
                                    println(exception)
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "There is not enough produce", Toast.LENGTH_SHORT).show()
                }
        }

        holder.binding.btnCartItemRemove.setOnClickListener {
            if (count > 1) {
                count--
            } else {
                count = 1
            }
            holder.binding.tvCartItemCount.text = count.toString()

            onQuantityChangeListener.onQuantityChanged(count)

            orderDatabaseReference.whereEqualTo("uid", currentItem.uid)
                .whereEqualTo("pid", currentItem.pid).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val productOrderModel: ProductOrderModel? =
                            document.toObject(ProductOrderModel::class.java)

                        if (productOrderModel != null) {
                            productOrderModel.quantity = count
                            orderDatabaseReference.document(document.id).set(productOrderModel)
                                .addOnSuccessListener {
                                    println("updated product")
                                }
                                .addOnFailureListener { exception ->
                                    println(exception)
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Min 1 product is Required", Toast.LENGTH_SHORT).show()
                }
        }

        holder.binding.deleteIcon.setOnClickListener {
            onItemClickRemove.onItemClick(currentItem)
            Toast.makeText(context, "Removed Successfully!", Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            for (i in 0 until list.size) {
                onItemClickRemove.onItemClick(list[i])
            }
            onNavigationClickListener.onNavigateToSuccessFragment()
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnItemClickListener {
        fun onItemClick(item: CardModel)
    }

    interface OnQuantityChangeListener {
        fun onQuantityChanged(newCount: Int): Double
    }

    interface OnNavigationClickListener {
        fun onNavigateToSuccessFragment()
    }

}