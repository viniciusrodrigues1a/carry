package com.viniciusrodriguesaro.carry.shoppingitem.utils

import java.text.NumberFormat
import java.text.ParseException

object priceFormatter {
    private val formatter = NumberFormat.getCurrencyInstance()

    fun parse(s: String): Int? {
        try {
            return (formatter.parse(s).toDouble() * 100).toInt()
        } catch (e: ParseException) {
            return null
        }
    }

    fun format(i: Int): String {
        return formatter.format(i / 100.0)
    }

    fun addOneDigit(s: String): Int? {
        val parsed = parse(s)
        val lastDigit = s?.lastOrNull().toString()

        return (parsed.toString() + lastDigit).toIntOrNull()
    }

    fun removeOneDigit(s: String): Int? {
        try {
            val parsed = parse(s)

            return parsed.toString().dropLast(1).toInt()
        } catch (e: java.lang.NumberFormatException) {
            return 0
        }
    }
}