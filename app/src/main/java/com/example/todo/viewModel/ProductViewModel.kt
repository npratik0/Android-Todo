package com.example.todo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo.model.ProductModel
import com.example.todo.repository.ProductRepository

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    val _product = MutableLiveData<ProductModel?>()
    val product: LiveData<ProductModel?> = _product

    val _allProducts = MutableLiveData<List<ProductModel>>()
    val allProducts: LiveData<List<ProductModel>> = _allProducts

    val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        _loadingState.value = true
        repository.addProduct(productModel) { success, message ->
            _loadingState.value = false
            callback(success, message)
        }
    }

    fun updateProduct(productId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        _loadingState.value = true
        repository.updateProduct(productId, data) { success, message ->
            _loadingState.value = false
            callback(success, message)
        }
    }

    fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        _loadingState.value = true
        repository.deleteProduct(productId) { success, message ->
            _loadingState.value = false
            callback(success, message)
        }
    }

    fun getProductById(productId: String) {
        _loadingState.value = true
        repository.getProductById(productId) { product, success, message ->
            _loadingState.value = false
            if (success) {
                _product.value = product
            } else {
                _product.value = null
            }
        }
    }

    fun getAllProducts() {
        _loadingState.value = true
        repository.getAllProduct { products, success, message ->
            _loadingState.value = false
            if (success) {
                _allProducts.value = products
            } else {
                _allProducts.value = emptyList()
            }
        }
    }

}