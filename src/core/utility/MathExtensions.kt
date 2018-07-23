package core.utility

fun max(vararg numbers: Int): Int {
    var max = numbers[0]
    numbers.forEach {
        max = Math.max(max, it)
    }
    return max
}