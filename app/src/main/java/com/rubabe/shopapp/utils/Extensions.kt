package com.rubabe.shopapp.utils

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigation
import com.rubabe.shopapp.data.model.BeautyDisplayModel
import com.rubabe.shopapp.data.repo.ProductsDaoRepository

object Extensions {

    fun Activity.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun Navigation.navigate(id: Int, bundle: Bundle, navOptions: NavOptions) {
        navigate(id, bundle, navOptions)
    }


}