package com.rubabe.shopapp.model

data class ProductOrderModel(
    val uid: String? = null,
    val pid: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val size: String? = null,
    var quantity: Int? = null,
    val price: String? = null

)
