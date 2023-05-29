package com.viniciusrodriguesaro.carry.shoppingitem.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UpdateShoppingItemInput

class ShoppingItemViewModel(private val repository: ShoppingItemRepository) : ViewModel() {
    private val uiState: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>(UiState(shoppingItemList = listOf()))
    }
    private var shoppingItemListId: String? = null

    init {
        if (shoppingItemListId != null) {
        repository.fetchShoppingItems(shoppingItemListId!!).addOnSuccessListener {
            uiState.value?.let { currentUiState ->
                uiState.value = currentUiState.copy(
                    shoppingItemList = it
                )
            }
        }
        }
    }

    fun stateOnceAndStream(): LiveData<UiState> {
        return uiState
    }

    fun setShoppingItemListId(id: String) {
        this.shoppingItemListId = id
        refreshShoppingItemList()
    }

    fun toggleShoppingItemCompleted(id: String) {
        if (shoppingItemListId == null) {
            return
        }

        repository.toggleShoppingItemCompleted(shoppingItemListId!!, id).addOnSuccessListener {
            refreshShoppingItemList()
        }
    }

    fun createShoppingItem(shoppingItemInput: CreateShoppingItemInput) {
        if (shoppingItemListId == null) {
            return
        }

        repository.createShoppingItem(shoppingItemListId!!, shoppingItemInput)
    }

    fun updateShoppingItem(shoppingItemInput: UpdateShoppingItemInput) {
        if (shoppingItemListId == null) {
            return
        }

        repository.updateShoppingItem(shoppingItemListId!!, shoppingItemInput)
    }

    fun deleteShoppingItem(shoppingItemId: String) {
        if (shoppingItemListId == null) {
            return
        }

        repository.deleteShoppingItem(shoppingItemListId!!, shoppingItemId)
    }

    private fun refreshShoppingItemList() {
        if (shoppingItemListId == null) {
            return
        }

        repository.fetchShoppingItems(shoppingItemListId!!).addOnSuccessListener {
            uiState.value?.let { currentUiState ->
                uiState.value = currentUiState.copy(
                    shoppingItemList = it
                )
            }
        }
    }

    data class UiState(val shoppingItemList: List<ShoppingItem>)

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: ShoppingItemRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShoppingItemViewModel(repository) as T
        }
    }
}