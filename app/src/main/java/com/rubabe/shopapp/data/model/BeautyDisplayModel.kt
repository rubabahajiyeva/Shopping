package com.rubabe.shopapp.data.model

data class BeautyDisplayModel(
    var brand: String? = null,
    val description: String? = null,
    val id: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val price: String? = null,
    var isLiked: Boolean = false,
    var isSelected:Boolean = false

){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BeautyDisplayModel

        // Compare based on the id property
        return brand == other.brand
    }

    override fun hashCode(): Int {
        return brand.hashCode()
    }
}