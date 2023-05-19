package com.viniciusrodriguesaro.carry.shoppingitem.ui

import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput

interface ShoppingItemListRepository {
    fun fetchShoppingItems(): List<ShoppingItem>

    fun toggleShoppingItemCompleted(id: String)

    fun createShoppingItem(shoppingItem: CreateShoppingItemInput)
}