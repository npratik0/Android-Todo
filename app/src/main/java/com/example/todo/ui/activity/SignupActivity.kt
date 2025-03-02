package com.example.todo.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todo.R
import com.example.todo.databinding.ActivitySignupBinding
import com.example.todo.model.UserModel
import com.example.todo.repository.UserRepository
import com.example.todo.repository.UserRepositoryImpl
import com.example.todo.utils.LoadingUtils
import com.example.todo.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var loadingUtils: LoadingUtils
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userRepository = UserRepositoryImpl()
        userViewModel = UserViewModel(userRepository)
        loadingUtils = LoadingUtils(this)

        binding.signupButton.setOnClickListener {
            loadingUtils.show()

            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val phone = binding.signupPhonenumber.text.toString()
            val fullname = binding.signupFullname.text.toString()

            if (!validateFields(context = this, email = email, password = password, phone = phone, fullname = fullname)) {
                loadingUtils.dismiss()
                return@setOnClickListener
            }

            userViewModel.signup(email, password) { success, message, userId ->
                if (success) {
                    val userModel = UserModel(userId, email, phone, fullname)
                    addUser(userModel)
                } else {
                    Toast.makeText(
                        this@SignupActivity,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingUtils.dismiss()
                }
            }
        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validateFields(context: SignupActivity, email: String, password: String, phone: String, fullname: String): Boolean {
        if (email.isEmpty()) {
            Toast.makeText(context, "Email field cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if (!email.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))) {
            Toast.makeText(context, "Provide valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(context, "Password field cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length < 8) {
            Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isEmpty()) {
            Toast.makeText(context, "Phone field cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        } else if (!phone.matches(Regex("\\d{10}"))) {
            Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (fullname.isEmpty()) {
            Toast.makeText(context, "Full name field cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun addUser(userModel: UserModel){
        userViewModel.addUserToDatabase(userModel.userID,userModel){
                success,message ->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(this@SignupActivity
                    ,message,Toast.LENGTH_SHORT).show()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(this@SignupActivity
                    ,message,Toast.LENGTH_SHORT).show()
            }
        }
    }

}