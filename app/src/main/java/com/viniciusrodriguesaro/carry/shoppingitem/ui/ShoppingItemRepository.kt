package com.viniciusrodriguesaro.carry.shoppingitem.ui

import com.google.android.gms.tasks.Task
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput

interface ShoppingItemListRepository {
    fun fetchShoppingItems(): Task<List<ShoppingItem>>

    fun toggleShoppingItemCompleted(id: String): Task<Unit>

    fun createShoppingItem(shoppingItem: CreateShoppingItemInput): Task<Unit>
}