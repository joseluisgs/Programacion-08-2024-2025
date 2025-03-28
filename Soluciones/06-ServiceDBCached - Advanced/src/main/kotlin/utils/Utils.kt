package dev.joseluisgs.utils

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.roundTo(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return (this * factor).roundToInt() / factor
}