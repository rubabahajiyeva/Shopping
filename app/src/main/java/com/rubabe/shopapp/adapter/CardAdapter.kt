package com.rubabe.shopapp.adapter

import com.rubabe.shopapp.databinding.CardProductItemBinding
import com.rubabe.shopapp.model.CardModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rubabe.shopapp.Delete


class CardAdapter(
    private val context: Context,
    private val list: ArrayList<CardModel>,
    private val onLongClickRemove: OnLongClickRemove

) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    var count = 2

    inner class ViewHolder(val binding: CardProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val onSwipeDelete = object : Delete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                list.removeAt(position)
            }
        }

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

        }

        holder.binding.btnCartItemRemove.setOnClickListener {
            if (count > 1) {
                count--
            } else {
                count = 1
            }

            holder.binding.tvCartItemCount.text = count.toString()
        }

        holder.itemView.setOnLongClickListener {
            onLongClickRemove.onLongRemove(currentItem, position)
            true
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    interface OnLongClickRemove {
        fun onLongRemove(item: CardModel, position: Int)
    }


}