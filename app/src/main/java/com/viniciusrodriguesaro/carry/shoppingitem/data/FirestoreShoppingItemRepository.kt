package com.viniciusrodriguesaro.carry.shoppingitem.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UpdateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.ui.ShoppingItem
import com.viniciusrodriguesaro.carry.shoppingitem.ui.interfaces.ShoppingItemRepository
import com.viniciusrodriguesaro.carry.shoppingitem.utils.MeasurementTypeConverter

class FirestoreShoppingItemRepository(private val measurementTypeConverter: MeasurementTypeConverter) :
    ShoppingItemRepository {
    private var SHOPPING_ITEM_LIST_COLLECTION_ID = "ShoppingItemList"
    private var SHOPPING_ITEM_COLLECTION_ID = "ShoppingItem"
    private var TAG = "FIRESTORE_SHOPPING_ITEM_REPOSITORY"

    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun fetchShoppingItems(shoppingItemListId: String): Task<List<ShoppingItem>> {
        val taskCompletionSource = TaskCompletionSource<List<ShoppingItem>>()

        val newShoppingItems: MutableList<ShoppingItem> = mutableListOf()

        val itemListCol = firestore.collection(SHOPPING_ITEM_LIST_COLLECTION_ID)

        itemListCol.document(shoppingItemListId).collection(SHOPPING_ITEM_COLLECTION_ID).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val rawUnitOfMeasurement = document.data["unit_of_measurement"] as String?
                    var parsedUnitOfMeasurement: MeasurementType? = null
                    if (rawUnitOfMeasurement != null) {
                        parsedUnitOfMeasurement =
                            measurementTypeConverter.stringToMeasurementType(rawUnitOfMeasurement)
                    }

                    val shoppingItem = document.data?.let {
                        ShoppingItem(
                            document.id,
                            it["is_completed"] as Boolean,
                            it["name"] as String,
                            it["description"] as String,
                            (it["price"] as Long?)?.toInt(),
                            (it["amount"] as Long?)?.toInt(),
                            parsedUnitOfMeasurement,
                            it["created_at"] as Long
                        )
                    }

                    Log.d(TAG, "ShoppingItem with name: ${shoppingItem?.name}")

                    if (shoppingItem != null) {
                        newShoppingItems.add(shoppingItem)
                    }
                }

                Log.d(TAG, "ShoppingItems size: ${newShoppingItems.size}")
                taskCompletionSource.setResult(newShoppingItems)
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error querying shopping items: $exception")
                taskCompletionSource.setException(exception)
            }

        return taskCompletionSource.task
    }

    override fun toggleShoppingItemCompleted(shoppingItemListId: String, id: String): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        val docRef =
            firestore
                .collection(SHOPPING_ITEM_LIST_COLLECTION_ID)
                .document(shoppingItemListId)
                .collection(SHOPPING_ITEM_COLLECTION_ID)
                .document(id)

        docRef.get().addOnSuccessListener {
            val oldState = it.data?.get("is_completed") as Boolean

            val updates = hashMapOf<String, Any>(
                "is_completed" to !oldState
            )

            docRef.update(updates).addOnSuccessListener {
                Log.d(TAG, "Update item: $id")
                taskCompletionSource.setResult(Unit)
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Error updating document: $exception")
                taskCompletionSource.setException(exception)
            }
        }

        return taskCompletionSource.task
    }

    override fun createShoppingItem(
        shoppingItemListId: String,
        shoppingItemInput: CreateShoppingItemInput
    ): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        val parentDocRef =
            firestore.collection(SHOPPING_ITEM_LIST_COLLECTION_ID).document(shoppingItemListId)

        val newDocRef = parentDocRef.collection(SHOPPING_ITEM_COLLECTION_ID).document()

        val data = hashMapOf(
            "is_completed" to false,
            "name" to shoppingItemInput.name,
            "description" to shoppingItemInput.description,
            "price" to shoppingItemInput.price,
            "amount" to shoppingItemInput.amount,
            "unit_of_measurement" to shoppingItemInput.unitOfMeasurementString,
            "created_at" to shoppingItemInput.createdAt
        )

        newDocRef.set(data)
            .addOnSuccessListener {
                Log.d(TAG, "New document created with id: ${newDocRef.id}")
                taskCompletionSource.setResult(Unit)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error creating document: $exception")
                taskCompletionSource.setException(exception)
            }

        return taskCompletionSource.task
    }

    override fun updateShoppingItem(
        shoppingItemListId: String,
        shoppingItemInput: UpdateShoppingItemInput
    ): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        val docRef =
            firestore
                .collection(SHOPPING_ITEM_LIST_COLLECTION_ID)
                .document(shoppingItemListId)
                .collection(SHOPPING_ITEM_COLLECTION_ID)
                .document(shoppingItemInput.id)

        val updatedData = hashMapOf<String, Any?>(
            "name" to shoppingItemInput.newName,
            "description" to shoppingItemInput.newDescription,
            "price" to shoppingItemInput.newPrice,
            "amount" to shoppingItemInput.newAmount,
            "unit_of_measurement" to shoppingItemInput.newUnitOfMeasurementString,
        )

        docRef.update(updatedData).addOnSuccessListener {
            Log.d(TAG, "Updated document with id: ${docRef.id}")
            taskCompletionSource.setResult(Unit)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error updating document: $exception")
            taskCompletionSource.setException(exception)
        }

        return taskCompletionSource.task
    }

    override fun deleteShoppingItem(
        shoppingItemListId: String,
        shoppingItemId: String
    ): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        val docRef =
            firestore
                .collection(SHOPPING_ITEM_LIST_COLLECTION_ID)
                .document(shoppingItemListId)
                .collection(SHOPPING_ITEM_COLLECTION_ID)
                .document(shoppingItemId)

        docRef.delete().addOnSuccessListener {
            Log.d(TAG, "Deleted document with id: ${docRef.id}")
            taskCompletionSource.setResult(Unit)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Error deleting document: ${docRef.id}")
            taskCompletionSource.setException(exception)
        }

        return taskCompletionSource.task
    }
}