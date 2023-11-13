package com.rubabe.shopapp.data.model

data class User(
    val firstName: String,
    val email: String,
    var imagePath: String = ""
) {
    constructor() : this("", "", "")

}