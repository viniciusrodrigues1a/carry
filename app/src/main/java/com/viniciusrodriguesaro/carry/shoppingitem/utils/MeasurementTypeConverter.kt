package com.viniciusrodriguesaro.carry.shoppingitem.utils

import android.content.Context
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement

class MeasurementTypeConverter(private val context: Context) {
    private val localizedStringsEnum = object {
        val LOCALIZED_UNIT = context.getString(R.string.unit_measurement)
        val LOCALIZED_L = context.getString(R.string.liter_measurement)
        val LOCALIZED_KG = context.getString(R.string.kilogram_measurement)
        val LOCALIZED_MG = context.getString(R.string.milligram_measurement)
        val LOCALIZED_G = context.getString(R.string.gram_measurement)
    }

    fun measurementTypeToLocalizedString(measurement: MeasurementType?): String {
        if (measurement is MeasurementType.EnumMeasurement) {
            return when ((measurement as MeasurementType.EnumMeasurement).value) {
                UnitOfMeasurement.UNIT -> context.getString(R.string.unit_measurement)
                UnitOfMeasurement.LITER -> context.getString(R.string.liter_measurement)
                UnitOfMeasurement.KILOGRAM -> context.getString(R.string.kilogram_measurement)
                UnitOfMeasurement.MILLIGRAM -> context.getString(R.string.milligram_measurement)
                UnitOfMeasurement.GRAM -> context.getString(R.string.gram_measurement)
            }
        } else if (measurement is MeasurementType.StringMeasurement) {
            return (measurement as MeasurementType.StringMeasurement).value
        } else {
            return context.getString(R.string.unit_measurement)
        }
    }


    fun localizedStringToMeasurementType(s: String): MeasurementType {
        return when (s) {
            localizedStringsEnum.LOCALIZED_UNIT -> MeasurementType.EnumMeasurement(UnitOfMeasurement.UNIT)
            localizedStringsEnum.LOCALIZED_L -> MeasurementType.EnumMeasurement(UnitOfMeasurement.LITER)
            localizedStringsEnum.LOCALIZED_KG -> MeasurementType.EnumMeasurement(UnitOfMeasurement.KILOGRAM)
            localizedStringsEnum.LOCALIZED_MG -> MeasurementType.EnumMeasurement(UnitOfMeasurement.MILLIGRAM)
            localizedStringsEnum.LOCALIZED_G -> MeasurementType.EnumMeasurement(UnitOfMeasurement.GRAM)
            else -> MeasurementType.StringMeasurement(s)
        }
    }
}