package com.rubabe.shopapp.data.model


data class LikeModel(
    val pid: String? = null,
    val uid: String? = null,
    val brand: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val price: String? = null,
    var isLiked: Boolean = false

)