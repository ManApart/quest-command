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
    return { _: String, newValue: String ->
        val intValue = newValue.toInt()
        max(minValue, min(maxValue, intValue)).toString()
    }
}

fun createAcceptedValuesList(acceptedValues: List<String>): (String, String) -> String {
    return { current: String, newValue: String ->
        if (acceptedValues.contains(newValue)) {
            newValue
        } else {
            current
        }
    }
}