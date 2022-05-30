package core.utility

actual fun currentTime(): Long {
    throw NotImplementedError()
}

actual fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V> {
    throw NotImplementedError()
}

actual fun <K,V> MutableMap<K,V>.putAbsent(key: K, value: V) {
    throw NotImplementedError()
}

actual object Math {
    actual fun random(): Double {
        TODO("Not yet implemented")
    }

    actual val PI: Double
        get() = TODO("Not yet implemented")

    actual fun toDegrees(radians: Double): Double {
        TODO("Not yet implemented")
    }
}

object Integer {
    fun parseInt(value: String?): Int {
        throw NotImplementedError()
    }
}