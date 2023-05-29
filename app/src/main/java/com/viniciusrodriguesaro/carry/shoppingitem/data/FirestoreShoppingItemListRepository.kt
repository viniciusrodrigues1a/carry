package com.viniciusrodriguesaro.carry.shoppingitem.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.viniciusrodriguesaro.carry.shoppingitem.ui.ShoppingItemListRepository

class FirestoreShoppingItemListRepository : ShoppingItemListRepository {
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth

    override fun getDefaultListId(): Task<String> {
        val taskCompletionSource = TaskCompletionSource<String>()

        val userId = auth.currentUser?.uid

        if (userId != null) {
            val itemListCol = firestore.collection(SHOPPING_ITEM_LIST_COLLECTION_ID)
            val query = itemListCol.whereEqualTo("owner_id", userId)
            query.get().addOnSuccessListener {
                if (it != null && !it.isEmpty) {
                    val doc = it.documents[0]

                    Log.d(TAG, "ShoppingItemList found: ${doc.id}")
                    taskCompletionSource.setResult(doc.id)
                } else {
                    taskCompletionSource.setException(Exception("Couldn't find shopping item list"))
                }
            }
        } else {
            taskCompletionSource.setException(Exception("Couldn't find current user"))
        }

        return taskCompletionSource.task
    }

    override fun createDefaultList(name: String): Task<String> {
        val taskCompletionSource = TaskCompletionSource<String>()

        val userId = auth.currentUser?.uid

        if (userId != null) {
            val newDocRef = firestore.collection(SHOPPING_ITEM_LIST_COLLECTION_ID).document()

            val data = hashMapOf(
                "name" to name,
                "owner_id" to auth.currentUser?.uid
            )

            newDocRef.set(data)
                .addOnSuccessListener {
                    Log.d(TAG, "New document created with id: ${newDocRef.id}")
                    taskCompletionSource.setResult(newDocRef.id)
                }.addOnFailureListener { exception ->
                    Log.d(
                        FirestoreShoppingItemRepository.TAG,
                        "Error creating document: $exception"
                    )
                    taskCompletionSource.setException(exception)
                }
        } else {
            taskCompletionSource.setException(Exception("Couldn't find current user"))
        }

        return taskCompletionSource.task
    }

    companion object {
        var SHOPPING_ITEM_LIST_COLLECTION_ID = "ShoppingItemList"
        var TAG = "FIRESTORE_SHOPPING_ITEM_LIST_REPOSITORY"
    }
}