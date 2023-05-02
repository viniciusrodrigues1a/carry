package com.viniciusrodriguesaro.carry.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    public lateinit var auth: FirebaseAuth

    public val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun initializeAuth() {
        auth = Firebase.auth
    }

    fun signUpWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("AUTH", "Created account")
                } else {
                    errorMessage.value = task.exception?.message ?: ""
                }
            }
    }
}