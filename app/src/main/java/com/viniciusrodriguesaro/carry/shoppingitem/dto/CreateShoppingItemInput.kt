package com.viniciusrodriguesaro.carry.shoppingitem.dto

data class CreateShoppingItemInput(
    var name: String,
    var description: String?,
    var price: Int?,
    var amount: Int?,
    var unitOfMeasurementString: String?
)