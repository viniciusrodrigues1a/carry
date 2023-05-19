package com.viniciusrodriguesaro.carry.shoppingitem.dto

data class CreateShoppingItemInput(
    var name: String,
    var description: String,
    var price: Double?,
    var amount: Int?,
    var unitOfMeasurement: MeasurementType?
)