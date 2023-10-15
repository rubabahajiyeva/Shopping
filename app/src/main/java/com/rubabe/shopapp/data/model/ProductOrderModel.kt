package com.rubabe.shopapp.data.model

data class ProductOrderModel(
    val uid: String? = null,
    val pid: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val type: String? = null,
    var quantity: Int? = null,
    val price: String? = null

)
