package com.viniciusrodriguesaro.carry.shoppingitem.dto

import android.content.Context
import com.viniciusrodriguesaro.carry.shoppingitem.utils.MeasurementTypeConverter

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
            val measurementTypeConverter = MeasurementTypeConverter(context)

            return UnitOfMeasurement.values()
                .map {
                    measurementTypeConverter.measurementTypeToLocalizedString(
                        MeasurementType.EnumMeasurement(
                            it
                        )
                    )
                }
                .toTypedArray()
        }
    }
}

