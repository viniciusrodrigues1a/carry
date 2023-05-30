package com.viniciusrodriguesaro.carry.shoppingitem.utils

import android.content.Context
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement


fun localizedStringToMeasurementType(context: Context, s: String): MeasurementType {
    val localizedStringsEnum = object {
        val LOCALIZED_UNIT = context.getString(R.string.unit_measurement)
        val LOCALIZED_L = context.getString(R.string.liter_measurement)
        val LOCALIZED_KG = context.getString(R.string.kilogram_measurement)
        val LOCALIZED_MG = context.getString(R.string.milligram_measurement)
        val LOCALIZED_G = context.getString(R.string.gram_measurement)
    }


    return when (s) {
        localizedStringsEnum.LOCALIZED_UNIT -> MeasurementType.EnumMeasurement(UnitOfMeasurement.UNIT)
        localizedStringsEnum.LOCALIZED_L -> MeasurementType.EnumMeasurement(UnitOfMeasurement.LITER)
        localizedStringsEnum.LOCALIZED_KG -> MeasurementType.EnumMeasurement(UnitOfMeasurement.KILOGRAM)
        localizedStringsEnum.LOCALIZED_MG -> MeasurementType.EnumMeasurement(UnitOfMeasurement.MILLIGRAM)
        localizedStringsEnum.LOCALIZED_G -> MeasurementType.EnumMeasurement(UnitOfMeasurement.GRAM)
        else -> MeasurementType.StringMeasurement(s)
    }
}
