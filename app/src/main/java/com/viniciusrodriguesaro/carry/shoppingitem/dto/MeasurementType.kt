package com.viniciusrodriguesaro.carry.shoppingitem.dto

import android.content.Context
import com.viniciusrodriguesaro.carry.shoppingitem.ui.utils.unitOfMeasurementToLocalizedString

sealed class MeasurementType {
    data class StringMeasurement(val value: String) : MeasurementType()
    data class EnumMeasurement(val value: UnitOfMeasurement) : MeasurementType()
}

enum class UnitOfMeasurement {
    UNIT,
    KILOGRAM,
    LITER,
    MILLIGRAM,
    GRAM;

    companion object {
        fun getLocalizedValues(context: Context): Array<String> {
            return UnitOfMeasurement.values()
                .map {
                    unitOfMeasurementToLocalizedString(
                        context,
                        MeasurementType.EnumMeasurement(it)
                    )
                }
                .toTypedArray()
        }
    }
}

