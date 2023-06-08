package com.viniciusrodriguesaro.carry.shoppingitem.dto

import android.content.Context
import android.view.accessibility.AccessibilityManager
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
                    val m = MeasurementType.EnumMeasurement(it)

                    if (isScreenReaderEnabled(context)) {
                        measurementTypeConverter.measurementTypeToAccessibleLocalizedString(m)
                    } else {
                        measurementTypeConverter.measurementTypeToLocalizedString(m)
                    }
                }
                .toTypedArray()
        }

        private fun isScreenReaderEnabled(context: Context): Boolean {
            val accessibilityManager =
                context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
            return accessibilityManager.isEnabled && accessibilityManager.isTouchExplorationEnabled
        }
    }
}

