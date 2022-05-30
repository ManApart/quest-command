package core.utility

expect fun currentTime(): Long
expect fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V>
expect fun <K, V> MutableMap<K, V>.putAbsent(key: K, value: V)

expect object Math {
    val PI: Double
    fun random(): Double
    fun toDegrees(radians: Double): Double
}

expect object Integer {
    fun parseInt(value: String?): Int
}