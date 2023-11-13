package com.rubabe.shopapp.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.rubabe.shopapp.data.model.BeautyDisplayModel
import com.rubabe.shopapp.R
import com.rubabe.shopapp.databinding.CategoryMainItemBinding


class MainCategoryAdapter(
    var context: Context,
    var list: LinkedHashSet<BeautyDisplayModel>,
    val onClickCategory: CategoryOnClickInterface,
) : RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {


    private var selectedPosition = RecyclerView.NO_POSITION // Initialize with a safe default value
    private lateinit var selectedButton: Button

    inner class ViewHolder(val binding: CategoryMainItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryMainItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list.elementAt(position)
        val categoryButton = holder.binding.btnItemCategory
        categoryButton.text = "${currentItem.brand}"


        val binding = holder.binding
        // Set the background color based on the isSelected property
        if (currentItem.isSelected) {
            categoryButton.setBackgroundResource(R.drawable.custom_button)
        } else {
            categoryButton.setBackgroundResource(R.drawable.custom_button_unselected)
        }

        categoryButton.setOnClickListener {

            val clickedPosition = holder.adapterPosition

            if (clickedPosition != RecyclerView.NO_POSITION) {
                if (selectedPosition != RecyclerView.NO_POSITION) {
                    list.elementAt(selectedPosition).isSelected = false
                    notifyItemChanged(selectedPosition)
                }


                currentItem.isSelected = true
                notifyItemChanged(clickedPosition)

                selectedPosition = clickedPosition
                selectedButton = categoryButton

                onClickCategory.onClickCategory(categoryButton)
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


}


interface CategoryOnClickInterface {
    fun onClickCategory(button: Button)
}

