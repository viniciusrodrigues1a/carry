package com.viniciusrodriguesaro.carry.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth

    val errorCode: MutableLiveData<String> by lazy {
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

    fun signUpWithEmailAndPassword(email: String, password: String, passwordConfirmation: String) {
        if (password != passwordConfirmation) {
            errorCode.value = "PASSWORDS_DO_NOT_MATCH"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Created account")
                } else {
                    handleFailedTask(task)
                }
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Logged in")
                } else {
                    handleFailedTask(task)
                }
            }
    }

    fun signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Logged in anonymously")
            } else {
                handleFailedTask(task)
            }
        }
    }

    private fun handleFailedTask(task: Task<AuthResult>) {
        val code = (task.exception as? FirebaseAuthException)?.errorCode.toString()
        var treatedCode = code

        if (task.exception is FirebaseNetworkException) {
            treatedCode = "ERROR_NETWORK_REQUEST_FAILED"
        }

        Log.d(TAG, "Error code: $treatedCode")
        errorCode.value = treatedCode
    }

    companion object {
        val TAG = "AUTH_VIEW_MODEL"
    }
}