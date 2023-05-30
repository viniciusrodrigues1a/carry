package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viniciusrodriguesaro.carry.shoppingitem.ui.interfaces.ShoppingItemListRepository

class ShoppingItemListViewModel(private val repository: ShoppingItemListRepository) : ViewModel() {
    private val uiState: MutableLiveData<UiState?> by lazy {
        MutableLiveData<UiState?>(null)
    }

    init {
        repository.getDefaultListId().addOnSuccessListener {
            Log.d(TAG, "Found default list of id: $it")
            uiState.value = UiState(it)
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Failed to find default list. Trying to create one")
            repository.createDefaultList("Lista padrão").addOnSuccessListener {
                uiState.value = UiState(it)
            }
        }
    }

    fun stateOnceAndStream(): LiveData<UiState?> {
        return uiState
    }

    data class UiState(val id: String)

    companion object {
        var TAG = "SHOPPING_ITEM_LIST_VIEW_MODEL"
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: ShoppingItemListRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShoppingItemListViewModel(repository) as T
        }
    }
}