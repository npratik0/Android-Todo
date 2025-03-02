package com.example.todo.viewModel

import android.service.autofill.UserData
import androidx.lifecycle.MutableLiveData
import com.example.todo.model.UserModel
import com.example.todo.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UserViewModel(val repo : UserRepository) {
    fun login(
        email : String ,
        password : String ,
        callback : (Boolean , String) -> Unit){
        repo.login(email, password , callback)
    }

    fun signup(
        email : String,
        password : String,
        callback: (Boolean, String, String) -> Unit
    ){
        repo.signup(email, password, callback)
    }

    fun forgetPassword(
        email : String,
        callback: (Boolean , String) -> Unit
    ){
        repo.forgetPassword(email , callback)
    }

    fun addUserToDatabase(
        userId : String,
        userModel : UserModel,
        callback: (Boolean, String ) -> Unit
    ){
        repo.addUserToDatabase(userId , userModel , callback)
    }

    fun getCurrentUser() : FirebaseUser?{
        return repo.getCurrentUser()
    }


    fun logout(callback: (Boolean, String) -> Unit) {
        repo.logout(callback)
    }

    fun editProfile(
        userId: String,
        data: MutableMap<String, Any>?,
        callback: (Boolean, String) -> Unit
    ) {
        repo.editProfile(userId , data , callback)
    }

    var _userData = MutableLiveData<UserModel?>()
    var userData = MutableLiveData<UserModel?>()
        get() = _userData

    fun getUserFromDatabase(
        userId: String
    ) {
        repo.getUserFromDatabase(userId){
                userModel, success, message ->
            if(success){

            }else{

            }
        }
    }

}