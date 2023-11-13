package com.rubabe.shopapp.ui.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rubabe.shopapp.data.model.LikeModel
import com.rubabe.shopapp.ui.viewmodel.LikedItemsViewModel
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.LikeItemBinding

class LikeAdapter(
    private val context: Context,
    private val list: LinkedHashSet<LikeModel>,
    private val productClickInterface: LikedProductOnClickInterface,
    private val likeClickInterface: LikedOnClickInterface,
    private val likedItemsViewModel: LikedItemsViewModel


) : RecyclerView.Adapter<LikeAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: LikeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding: LikeItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.like_item, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list.elementAt(position)
        holder.binding.product = currentItem
        holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.RED)

        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivBeautyDisplayItem)

        if (currentItem.pid?.let { likedItemsViewModel.isItemLiked(it) } == true) {
            holder.binding.btnLike.setBackgroundResource(R.drawable.like_heart_icon)
        } else {
            holder.binding.btnLike.setBackgroundResource(R.drawable.unlike_heart_icon)
        }



        holder.binding.btnLike.setOnClickListener {
            likeClickInterface.onClickLike(currentItem)
            if (!currentItem.isLiked) {
                holder.binding.btnLike.setBackgroundResource(R.drawable.like_heart_icon)
                currentItem.pid?.let { it1 -> likedItemsViewModel.setLikedItem(it1, true) }
                currentItem.isLiked = true
            } else {
                holder.binding.btnLike.setBackgroundResource(R.drawable.unlike_heart_icon)
                currentItem.pid?.let { it1 -> likedItemsViewModel.setLikedItem(it1, false) }
                currentItem.isLiked = false
            }
        }



        holder.itemView.setOnClickListener {
            productClickInterface.onClickProduct(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}

interface LikedProductOnClickInterface {
    fun onClickProduct(item: LikeModel)
}

interface LikedOnClickInterface {
    fun onClickLike(item: LikeModel)
}