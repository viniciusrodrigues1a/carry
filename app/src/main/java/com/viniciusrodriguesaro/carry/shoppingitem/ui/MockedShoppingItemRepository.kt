package com.viniciusrodriguesaro.carry.shoppingitem.ui

object MockedShoppingItemRepository : ShoppingItemListRepository {
    private val shoppingItemList: MutableList<ShoppingItem> = mutableListOf(
        ShoppingItem(
            "123",
            true,
            "Milk",
            "Fresh whole milk",
            2.99,
            1,
            MeasurementType.StringMeasurement("Boxes")
        ),
        ShoppingItem(
            "124", false, "Bread", "Whole wheat bread", 1.99, 1, MeasurementType.EnumMeasurement(
                UnitOfMeasurement.UNIT
            )
        ),
        ShoppingItem("125", false, "Eggs", "Farm-fresh eggs", null, null, null),
        ShoppingItem(
            "126", true, "Apples", "Organic red apples", 0.99, 6, MeasurementType.EnumMeasurement(
                UnitOfMeasurement.KILOGRAM
            )
        ),
        ShoppingItem(
            "127",
            false,
            "Chicken",
            "Boneless chicken breasts",
            5.99,
            2,
            MeasurementType.EnumMeasurement(
                UnitOfMeasurement.KILOGRAM
            )
        )
    )


    override fun fetchShoppingItems() = shoppingItemList.map { it.copy() }

    override fun toggleShoppingItemCompleted(id: String) {
        val index = shoppingItemList.indexOfFirst { it.id == id }
        val item = shoppingItemList[index]
        shoppingItemList[index] = item.copy(isCompleted = !item.isCompleted)
    }
}