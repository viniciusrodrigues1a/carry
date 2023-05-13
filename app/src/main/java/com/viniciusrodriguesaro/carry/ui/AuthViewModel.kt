package com.viniciusrodriguesaro.carry.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currentUser: MutableLiveData<FirebaseUser?> by lazy {
        MutableLiveData<FirebaseUser?>()
    }

    fun initializeAuth() {
        auth = Firebase.auth
        auth.addAuthStateListener {
            currentUser.value = it.currentUser
        }
    }

    fun signUpWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AUTH", "Created account")
                    onSuccess();
                } else {
                    errorMessage.value =
                        task.exception?.message ?: "Não foi possível fazer o cadastro"
                }
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("AUTH", "Logged in")
                } else {
                    errorMessage.value = task.exception?.message ?: "Não foi possível fazer o login"
                }
            }
    }

    fun signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("AUTH", "Logged in anonymously")
            } else {
                errorMessage.value =
                    task.exception?.message ?: "Não foi possível fazer o login anônimo"
            }
        }
    }
}