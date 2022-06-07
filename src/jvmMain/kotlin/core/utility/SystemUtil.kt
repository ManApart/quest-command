package core.utility

import kotlin.system.exitProcess
import kotlin.collections.toSortedMap as sorted

actual fun currentTime(): Long {
    return System.currentTimeMillis()
}

actual fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V> {
    return this.sorted()
}

actual fun <K, V> MutableMap<K, V>.putAbsent(key: K, value: V) {
    this.putIfAbsent(key, value)
}

actual fun exit() {
    exitProcess(0)
}

actual object Integer {
    actual fun parseInt(value: String?): Int {
        return java.lang.Integer.parseInt(value)
    }
}