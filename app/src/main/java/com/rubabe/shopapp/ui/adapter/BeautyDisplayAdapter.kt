package com.rubabe.shopapp.ui.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rubabe.shopapp.ui.viewmodel.LikedItemsViewModel
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.BeautyDisplayItemBinding
import com.rubabe.shopapp.data.model.BeautyDisplayModel

class BeautyDisplayAdapter(
    private val context: Context,
    var list: List<BeautyDisplayModel>,
    private val productClickInterface: ProductOnClickInterface,
    private val likeClickInterface: LikeOnClickInterface,
    private val likedItemsViewModel: LikedItemsViewModel
) : RecyclerView.Adapter<BeautyDisplayAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: BeautyDisplayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding: BeautyDisplayItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.beauty_display_item, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.product = currentItem

        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivBeautyDisplayItem)


        holder.itemView.setOnClickListener {
            productClickInterface.onClickProduct(list[position])
        }

        if (currentItem.id?.let { likedItemsViewModel.isItemLiked(it) } == true) {
            holder.binding.btnLike.setBackgroundResource(R.drawable.like_heart_icon)
        } else {
            holder.binding.btnLike.setBackgroundResource(R.drawable.unlike_heart_icon)
        }


        holder.binding.btnLike.setOnClickListener {
            val isLiked = currentItem.id?.let { it1 -> likedItemsViewModel.isItemLiked(it1) }
            if (!isLiked!!) {
                holder.binding.btnLike.setBackgroundResource(R.drawable.like_heart_icon)
                likeClickInterface.onClickLike(currentItem)
                currentItem.id.let { it1 -> likedItemsViewModel.setLikedItem(it1, true) }
            } else {
                holder.binding.btnLike.setBackgroundResource(R.drawable.unlike_heart_icon)
                currentItem.id.let { it1 -> likedItemsViewModel.setLikedItem(it1, false) }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }


}

interface ProductOnClickInterface {
    fun onClickProduct(item: BeautyDisplayModel)
}

interface LikeOnClickInterface {
    fun onClickLike(item: BeautyDisplayModel)
}
