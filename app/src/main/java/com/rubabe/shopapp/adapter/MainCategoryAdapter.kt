package com.rubabe.shopapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.rubabe.shopapp.databinding.CategoryMainItemBinding

class MainCategoryAdapter(
    private val list: LinkedHashSet<String>,
    val onClickCategory: CategoryOnClickInterface
) : RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {


    class ViewHolder(val binding: CategoryMainItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryMainItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.btnItemCategory.text = list.elementAt(position)


        holder.binding.btnItemCategory.setOnClickListener {
            onClickCategory.onClickCategory(holder.binding.btnItemCategory)
        }

        list

    }

    override fun getItemCount(): Int {
        return list.size
    }


}

interface CategoryOnClickInterface {
    fun onClickCategory(button: Button)
}