package com.example.todo.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.databinding.ActivityAddProductBinding
import com.example.todo.model.ProductModel
import com.example.todo.repository.ProductRepositoryImpl
import com.example.todo.utils.LoadingUtils
import com.example.todo.viewModel.ProductViewModel

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)

        val repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)

        binding.submitButton.setOnClickListener {
            loadingUtils.show()
            val productName = binding.productName.text.toString()
            val productPrice = binding.productPrice.text.toString()
            val productDesc = binding.productDescription.text.toString()
            val model = ProductModel("", productName, productDesc, productPrice)

            productViewModel.addProduct(model) { success, message ->
                loadingUtils.dismiss()
                Toast.makeText(this@AddProductActivity, message, Toast.LENGTH_SHORT).show()
                if (success) finish()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}