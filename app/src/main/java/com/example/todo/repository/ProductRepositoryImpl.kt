package com.example.todo.repository

import android.provider.ContactsContract.Data
import com.example.todo.model.ProductModel
import com.example.todo.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductRepositoryImpl : ProductRepository {
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference : DatabaseReference = database.reference.child("products")

    override fun addProduct(productModel: ProductModel, callback: (Boolean, String) -> Unit) {
        var id = reference.push().key.toString()
        productModel.productId = id
        reference.child(id).setValue(productModel).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true, "Task added successfully")
            }else{
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun updateProduct(productId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        reference.child(productId).updateChildren(data).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true, "Task updated successfully")
            }else{
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun deleteProduct(productId: String, callback: (Boolean, String) -> Unit) {
        reference.child(productId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Task deleted successfully")
            } else {
                callback(false, "${it.exception?.message}")
            }
        }
    }

    override fun getProductById(
        productId: String,
        callback: (ProductModel?, Boolean, String) -> Unit) {
        reference.child(productId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    var model = snapshot.getValue(ProductModel::class.java)
                    callback(model,true,"Data fetched success")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null , false , error.message.toString())
            }
        })
    }

    override fun getAllProduct(callback: (List<ProductModel>, Boolean, String) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<ProductModel>()
                if (snapshot.exists()) {
                    for (eachProduct in snapshot.children) {
                        val model = eachProduct.getValue(ProductModel::class.java)
                        if (model != null) {
                            products.add(model)
                        }
                    }
                    callback(products, true, "Data fetched successfully")
                } else {
                    callback(emptyList(), false, "No task found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList(), false, error.message.toString())
            }
        })
    }

}