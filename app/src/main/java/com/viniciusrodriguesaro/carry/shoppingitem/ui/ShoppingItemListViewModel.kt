package com.viniciusrodriguesaro.carry.shoppingitem.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShoppingItemListViewModel(private val repository: ShoppingItemListRepository) : ViewModel() {
    private val uiState: MutableLiveData<UiState> by lazy {
        MutableLiveData<UiState>(UiState(shoppingItemList = repository.fetchShoppingItems()))
    }

    fun stateOnceAndStream(): LiveData<UiState> {
        return uiState
    }

    fun toggleShoppingItemCompleted(id: String) {
        repository.toggleShoppingItemCompleted(id)
        refreshShoppingItemList()
    }

    private fun refreshShoppingItemList() {
        uiState.value?.let { currentUiState ->
            uiState.value = currentUiState.copy(
                shoppingItemList = repository.fetchShoppingItems()
            )
        }
    }

    data class UiState(val shoppingItemList: List<ShoppingItem>)

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: ShoppingItemListRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShoppingItemListViewModel(repository) as T
        }
    }
}