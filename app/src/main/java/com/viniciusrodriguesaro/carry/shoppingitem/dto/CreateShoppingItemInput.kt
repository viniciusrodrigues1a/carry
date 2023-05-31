package com.viniciusrodriguesaro.carry.shoppingitem.dto

import java.util.Date

data class CreateShoppingItemInput(
    var name: String,
    var description: String?,
    var price: Int?,
    var amount: Int?,
    var unitOfMeasurementString: String?,
    var createdAt: Long
)