package com.viniciusrodriguesaro.carry.shoppingitem.dto

data class UpdateShoppingItemInput(
    var id: String,
    var newName: String?,
    var newDescription: String?,
    var newPrice: Int?,
    var newAmount: Int?,
    var newUnitOfMeasurentString: String?
)