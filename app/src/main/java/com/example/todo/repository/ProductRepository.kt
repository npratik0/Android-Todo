package com.example.todo.repository

import android.telecom.CallEventCallback

import com.example.todo.model.ProductModel

interface ProductRepository {

    fun addProduct(productModel: ProductModel, callback : (Boolean , String) -> Unit)
    fun updateProduct(productId : String , data : MutableMap<String,Any>,
                      callback: (Boolean, String) -> Unit)
    fun deleteProduct(productId: String , callback: (Boolean, String) -> Unit)
    fun getProductById(productId: String , callback: (ProductModel? ,Boolean, String) -> Unit)
    fun getAllProduct(callback: (List<ProductModel>, Boolean, String) -> Unit)
}