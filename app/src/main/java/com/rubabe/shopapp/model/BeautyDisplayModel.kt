package com.rubabe.shopapp.model

data class BeautyDisplayModel(
    val brand: String? = null,
    val description: String? = null,
    val id: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val price: String? = null,
    var isLiked: Boolean = false

)