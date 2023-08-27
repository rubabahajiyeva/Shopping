package com.rubabe.shopapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rubabe.shopapp.databinding.CategoryMainItemBinding
import android.content.Context
import com.rubabe.shopapp.R


class MainCategoryAdapter(
    var context:Context,
    var list: LinkedHashSet<String>,
    val onClickCategory: CategoryOnClickInterface
) : RecyclerView.Adapter<MainCategoryAdapter.ViewHolder>() {
    private var selectedButton: TextView? = null
    private var originalButtonBackground: Drawable? = null

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

        val categoryButton = holder.binding.btnItemCategory
        categoryButton.text = list.elementAt(position)

        originalButtonBackground = categoryButton.background

        categoryButton.setOnClickListener {
            onClickCategory.onClickCategory(categoryButton)
            selectButton(categoryButton)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun selectButton(button: TextView) {
        selectedButton?.apply {
            isEnabled = true
            background = originalButtonBackground
        }

        // Disable and update selected button
        button.isEnabled = false
        selectedButton = button

        // Set background for selected button
        context.let {
            button.setBackgroundResource(R.drawable.custom_button)
        }
    }
}



interface CategoryOnClickInterface {
    fun onClickCategory(button: Button)
}

