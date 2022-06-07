package core.utility

import kotlin.js.Date

actual fun currentTime(): Long {
    return Date().getTime().toLong()
}

actual fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V> {
    return this.entries.sortedBy { it.key }.associate { it.key to it.value }
}

actual fun <K,V> MutableMap<K,V>.putAbsent(key: K, value: V) {
    if (!containsKey(key)) put(key, value)
}

actual fun exit(){
    println("Exited")
}

actual object Integer {
    actual fun parseInt(value: String?): Int {
        return parseInt(value)
    }
}