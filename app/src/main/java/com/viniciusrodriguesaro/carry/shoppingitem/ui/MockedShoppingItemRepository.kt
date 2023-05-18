package com.viniciusrodriguesaro.carry.shoppingitem.ui

object MockedShoppingItems {
    val mockedShoppingItems: List<ShoppingItem> = listOf(
        ShoppingItem(true, "Milk", "Fresh whole milk", 2.99, 1, MeasurementType.StringMeasurement("Boxes")),
        ShoppingItem(false, "Bread", "Whole wheat bread", 1.99, 1, MeasurementType.EnumMeasurement(
            UnitOfMeasurement.UNIT)),
        ShoppingItem(false, "Eggs", "Farm-fresh eggs", null, null, null),
        ShoppingItem(true, "Apples", "Organic red apples", 0.99, 6, MeasurementType.EnumMeasurement(
            UnitOfMeasurement.KILOGRAM)),
        ShoppingItem(false, "Chicken", "Boneless chicken breasts", 5.99, 2, MeasurementType.EnumMeasurement(
            UnitOfMeasurement.KILOGRAM))
    )
}