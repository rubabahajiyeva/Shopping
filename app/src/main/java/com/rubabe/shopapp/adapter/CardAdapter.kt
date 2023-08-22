package com.rubabe.shopapp.adapter

import android.annotation.SuppressLint
import com.rubabe.shopapp.databinding.CardProductItemBinding
import com.rubabe.shopapp.model.CardModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.rubabe.shopapp.fragment.CardPageFragment
import com.rubabe.shopapp.utils.Delete


class CardAdapter(
    private val context: Context,
    private val list: ArrayList<CardModel>,
    private val onItemClickRemove: OnItemClickListener,
    private val onQuantityChangeListener: OnQuantityChangeListener
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    var count = 1

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
           // Notify the listener
        }

        holder.binding.btnCartItemRemove.setOnClickListener {
            if (count > 1) {
                count--
            } else {
                count = 1
            }

            holder.binding.tvCartItemCount.text = count.toString()
            onQuantityChangeListener.onQuantityChanged(count) // Notify the listener
        }

        holder.binding.deleteIcon.setOnClickListener {
            onItemClickRemove.onItemClick(currentItem, position)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnItemClickListener {
        fun onItemClick(item: CardModel, position: Int)
    }

    interface OnQuantityChangeListener {
        fun onQuantityChanged(newCount: Int):Double
    }

}