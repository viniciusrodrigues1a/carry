package com.viniciusrodriguesaro.carry.shoppingitem.ui

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.viniciusrodriguesaro.carry.shoppingitem.dto.CreateShoppingItemInput
import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UpdateShoppingItemInput

object MockedShoppingItemRepository : ShoppingItemRepository {
    private val shoppingItemList: MutableList<ShoppingItem> = mutableListOf(
        ShoppingItem(
            "123",
            true,
            "Milk",
            "Fresh whole milk",
            299,
            1,
            MeasurementType.StringMeasurement("Boxes")
        ),
        ShoppingItem(
            "124", false, "Bread", "Whole wheat bread", 199, 1, MeasurementType.EnumMeasurement(
                UnitOfMeasurement.UNIT
            )
        ),
        ShoppingItem("125", false, "Eggs", "Farm-fresh eggs", null, null, null),
        ShoppingItem(
            "126", true, "Apples", "Organic red apples", 99, 6, MeasurementType.EnumMeasurement(
                UnitOfMeasurement.KILOGRAM
            )
        ),
        ShoppingItem(
            "127",
            false,
            "Chicken",
            "Boneless chicken breasts",
            99,
            2,
            MeasurementType.EnumMeasurement(
                UnitOfMeasurement.KILOGRAM
            )
        )
    )

    override fun fetchShoppingItems(shoppingItemListId: String): Task<List<ShoppingItem>> {
        val taskCompletionSource = TaskCompletionSource<List<ShoppingItem>>()

        taskCompletionSource.setResult(shoppingItemList.map { it.copy() })

        return taskCompletionSource.task
    }

    override fun toggleShoppingItemCompleted(shoppingItemListId: String, id: String): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        val index = shoppingItemList.indexOfFirst { it.id == id }
        val item = shoppingItemList[index]
        shoppingItemList[index] = item.copy(isCompleted = !item.isCompleted)

        taskCompletionSource.setResult(Unit)

        return taskCompletionSource.task
    }

    override fun createShoppingItem(shoppingItemListId: String, dto: CreateShoppingItemInput): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        val item = ShoppingItem(
            id = "129",
            isCompleted = false,
            name = dto.name,
            description = dto.description,
            price = dto.price,
            amount = dto.amount,
            unitOfMeasurement = MeasurementType.StringMeasurement(dto.unitOfMeasurementString ?: "")

        )

        shoppingItemList.add(item)

        taskCompletionSource.setResult(Unit)

        return taskCompletionSource.task
    }

    override fun updateShoppingItem(
        shoppingItemListId: String,
        shoppingItemInput: UpdateShoppingItemInput
    ): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        taskCompletionSource.setResult(Unit)

        return taskCompletionSource.task
    }

    override fun deleteShoppingItem(
        shoppingItemListId: String,
        shoppingItemId: String
    ): Task<Unit> {
        val taskCompletionSource = TaskCompletionSource<Unit>()

        taskCompletionSource.setResult(Unit)

        return taskCompletionSource.task
    }
}