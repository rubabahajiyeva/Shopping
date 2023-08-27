package com.rubabe.shopapp

import androidx.lifecycle.ViewModel

class LikedItemsViewModel : ViewModel() {
    private val likedItemsMap: MutableMap<String, Boolean> = mutableMapOf()

    fun setLikedItem(itemId: String, isLiked: Boolean) {
        likedItemsMap[itemId] = isLiked
    }

    fun isItemLiked(itemId: String): Boolean {
        return likedItemsMap[itemId] ?: false
    }
}
