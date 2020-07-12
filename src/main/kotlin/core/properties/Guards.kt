package core.properties

import kotlin.math.max
import kotlin.math.min

fun isGreaterThanEqualToZero(current: String, value: String): String {
    return if (value.toInt() >= 0) {
        value
    } else {
        "0"
    }
}

fun createNumberRange(minValue: Int, maxValue: Int): (String, String) -> String {
    return { current: String, value: String ->
        val intValue = value.toInt()
        max(minValue, min(maxValue, intValue)).toString()
    }
}