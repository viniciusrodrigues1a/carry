package com.viniciusrodriguesaro.carry.shoppingitem.utils

import android.content.Context
import com.viniciusrodriguesaro.carry.R
import com.viniciusrodriguesaro.carry.shoppingitem.dto.MeasurementType
import com.viniciusrodriguesaro.carry.shoppingitem.dto.UnitOfMeasurement

fun measurementTypeToLocalizedString(context: Context, measurement: MeasurementType?): String {
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
