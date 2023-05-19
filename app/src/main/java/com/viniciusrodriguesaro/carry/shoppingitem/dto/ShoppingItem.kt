package com.viniciusrodriguesaro.carry.shoppingitem.ui

import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType

data class ShoppingItem(
    var id: String,
    var isCompleted: Boolean,
    var name: String,
    var description: String,
    var price: Double?,
    var amount: Int?,
    var unitOfMeasurement: MeasurementType?
)
