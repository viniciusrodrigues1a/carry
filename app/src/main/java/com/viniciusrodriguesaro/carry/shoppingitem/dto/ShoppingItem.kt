package com.viniciusrodriguesaro.carry.shoppingitem.ui

import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import java.util.Date

data class ShoppingItem(
    var id: String,
    var isCompleted: Boolean,
    var name: String,
    var description: String?,
    var price: Int?,
    var amount: Int?,
    var unitOfMeasurement: MeasurementType?,
    var createdAt: Long
)