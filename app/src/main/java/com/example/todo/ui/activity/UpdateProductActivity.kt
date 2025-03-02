package com.example.todo.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.R
import com.example.todo.databinding.ActivityUpdateProductBinding
import com.example.todo.repository.ProductRepositoryImpl
import com.example.todo.viewModel.ProductViewModel

class UpdateProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)

        val productId: String? = intent.getStringExtra("productId")

        productViewModel.getProductById(productId.toString())
        productViewModel.product.observe(this) { product ->
            if (product != null) {
                binding.updateProductDescription.setText(product.productDesc)
                binding.updateProductPrice.setText(product.price)
                binding.updateProductName.setText(product.productName)
            } else {
                Toast.makeText(this, "Password not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.updateButton.setOnClickListener {
            val productName = binding.updateProductName.text.toString()
            val price = binding.updateProductPrice.text.toString()
            val desc = binding.updateProductDescription.text.toString()

            if (productName.isNotBlank() && price.isNotBlank()&& desc.isNotBlank()) {
                val updatedMap = mutableMapOf<String, Any>(
                    "productName" to productName,
                    "productDesc" to desc,
                    "price" to price
                )

                productViewModel.updateProduct(productId.toString(), updatedMap) { success, message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    if (success) {
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}