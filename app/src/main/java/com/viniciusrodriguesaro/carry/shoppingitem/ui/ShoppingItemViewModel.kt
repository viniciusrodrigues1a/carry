package com.viniciusrodriguesaro.carry.shoppingitem.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput

class ShoppingItemListViewModel(private val repository: ShoppingItemRepository) : ViewModel() {
    private val uiState: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>(UiState(shoppingItemList = listOf()))
    }

    init {
        repository.fetchShoppingItems().addOnSuccessListener {
            uiState.value?.let { currentUiState ->
                uiState.value = currentUiState.copy(
                    shoppingItemList = it
                )
            }
        }
    }

    fun stateOnceAndStream(): LiveData<UiState> {
        return uiState
    }

    fun toggleShoppingItemCompleted(id: String) {
        repository.toggleShoppingItemCompleted(id).addOnSuccessListener {
            refreshShoppingItemList()
        }
    }

    fun createShoppingItem(shoppingItemInput: CreateShoppingItemInput) {
        repository.createShoppingItem(shoppingItemInput).addOnSuccessListener {
            refreshShoppingItemList()
        }
    }

    private fun refreshShoppingItemList() {
        repository.fetchShoppingItems().addOnSuccessListener {
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
            return ShoppingItemListViewModel(repository) as T
        }
    }
}