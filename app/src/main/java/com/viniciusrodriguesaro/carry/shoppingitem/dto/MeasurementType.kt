package com.viniciusrodriguesaro.carry.shoppingitem.dto

import android.content.Context
import com.viniciusrodriguesaro.carry.shoppingitem.ui.utils.measurementTypeToLocalizedString

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
                    measurementTypeToLocalizedString(
                        context,
                        MeasurementType.EnumMeasurement(it)
                    )
                }
                .toTypedArray()
        }
    }
}

