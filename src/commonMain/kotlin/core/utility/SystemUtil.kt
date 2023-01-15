package core.utility

expect fun currentTime(): Long
expect fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V>
expect fun <K, V> MutableMap<K, V>.putAbsent(key: K, value: V)
expect fun exit()

expect object Integer {
    fun parseInt(value: String?): Int
}

//Allows for applying when the lambda needs to be a suspend
suspend inline fun <T> T.applySuspending(block: suspend T.() -> Unit): T {
    return this.also { it.block() }
}