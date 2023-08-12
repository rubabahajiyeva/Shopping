package com.rubabe.shopapp.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rubabe.shopapp.databinding.BeautyDisplayItemBinding
import com.rubabe.shopapp.model.BeautyDisplayModel

class BeautyDisplayAdapter(
    private val context:Context ,
    private val list: List<BeautyDisplayModel>,
    private val productClickInterface: ProductOnClickInterface,
    private val likeClickInterface: LikeOnClickInterface,

    ) : RecyclerView.Adapter<BeautyDisplayAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: BeautyDisplayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BeautyDisplayItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.binding.tvNameBeautyDisplayItem.text = "${currentItem.brand} ${currentItem.name}"
        holder.binding.tvPriceBeautyDisplayItem.text = "₹${currentItem.price}"


        Glide
            .with(context)
            .load(currentItem.imageUrl)
            .into(holder.binding.ivShoeDisplayItem)


        holder.itemView.setOnClickListener {
            productClickInterface.onClickProduct(list[position])
        }

        holder.binding.btnLike.setOnClickListener {
            if(holder.binding.btnLike.isChecked){
                holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.RED)
                likeClickInterface.onClickLike(currentItem)
            }
            else{
                holder.binding.btnLike.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
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

interface LikeOnClickInterface{
    fun onClickLike(item :BeautyDisplayModel)
}