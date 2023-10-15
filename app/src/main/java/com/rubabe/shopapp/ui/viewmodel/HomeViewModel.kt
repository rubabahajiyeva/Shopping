package com.rubabe.shopapp.ui.viewmodel

import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rubabe.shopapp.data.model.BeautyDisplayModel
import com.rubabe.shopapp.data.repo.ProductsDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var pRepo: ProductsDaoRepository) : ViewModel() {
    var categoryList: MutableLiveData<LinkedHashSet<BeautyDisplayModel>>
    var productList: MutableLiveData<ArrayList<BeautyDisplayModel>>



    init {

        addCategory()
        categoryList = pRepo.getCategory()
        productList = pRepo.getProduct()
    }


    fun click(item: BeautyDisplayModel) {
        pRepo.onClickLike(item)
    }


     fun addCategory() {
        pRepo.setList()
    }


    fun onClickCategory(button: Button) {
        pRepo.onClickCategory(button)
    }
}