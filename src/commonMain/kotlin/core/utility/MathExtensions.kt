package core.utility

import kotlin.math.min
import kotlin.random.Random.Default.nextInt

fun max(vararg numbers: Int): Int {
    var max = numbers[0]
    numbers.forEach {
        max = kotlin.math.max(max, it)
    }
    return max
}

fun random(): Double {
    return nextInt(0, 100) / 100.0
}

fun getRandomRange(max: Int, min: Int = 0): Int {
    return nextInt(max - min) + min
}

fun toDegrees(radians: Double): Double {
    return radians * 57.29577951308232
}

fun Int.clamp(min: Int, max: Int): Int {
    return max(min, min(max, this))
}