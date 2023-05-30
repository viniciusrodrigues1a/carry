package com.viniciusrodriguesaro.carry.shoppingitem.ui.interfaces

import com.google.android.gms.tasks.Task

interface ShoppingItemListRepository {
    fun getDefaultListId(): Task<String>

    fun createDefaultList(name: String): Task<String>
}