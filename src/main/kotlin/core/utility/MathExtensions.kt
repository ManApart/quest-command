package core.utility

import java.util.*

fun max(vararg numbers: Int): Int {
    var max = numbers[0]
    numbers.forEach {
        max = kotlin.math.max(max, it)
    }
    return max
}

fun getRandomRange(max: Int, min: Int = 0) : Int {
    return Random().nextInt(max-min) + min
}