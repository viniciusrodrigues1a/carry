package com.viniciusrodriguesaro.carry.shoppingitem.ui

import com.google.android.gms.tasks.Task
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UpdateShoppingItemInput

interface ShoppingItemRepository {
    fun fetchShoppingItems(shoppingItemListId: String): Task<List<ShoppingItem>>

    fun toggleShoppingItemCompleted(shoppingItemListId: String, id: String): Task<Unit>

    fun createShoppingItem(shoppingItemListId: String, shoppingItem: CreateShoppingItemInput): Task<Unit>

    fun updateShoppingItem(shoppingItemListId: String, shoppingItemInput: UpdateShoppingItemInput): Task<Unit>

    fun deleteShoppingItem(shoppingItemListId: String, shoppingItemId: String): Task<Unit>
}