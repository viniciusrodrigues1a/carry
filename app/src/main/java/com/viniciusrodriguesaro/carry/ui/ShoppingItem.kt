package com.viniciusrodriguesaro.carry.ui

data class ShoppingItem(
    var isChecked: Boolean,
    var name: String,
    var description: String,
    var price: Double?,
    var amount: Int?,
    var unitOfMeasurement: MeasurementType?
)

sealed class MeasurementType {
    data class StringMeasurement(val value: String) : MeasurementType()
    data class EnumMeasurement(val value: UnitOfMeasurement) : MeasurementType()
}

enum class UnitOfMeasurement {
    UNIT,
    KILOGRAM,
    LITER,
    MILLIGRAM,
    GRAM
}

