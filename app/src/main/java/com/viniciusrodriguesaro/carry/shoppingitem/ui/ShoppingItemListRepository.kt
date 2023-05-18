package com.viniciusrodriguesaro.carry.shoppingitem.ui

interface ShoppingItemListRepository {
    fun fetchShoppingItems(): List<ShoppingItem>

    fun toggleShoppingItemCompleted(id: String)
}