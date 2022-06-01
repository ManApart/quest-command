package core.utility

import core.events.EventListener
import io.ktor.client.*
import kotlin.reflect.KClass

expect fun currentTime(): Long
expect fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V>
expect fun <K, V> MutableMap<K, V>.putAbsent(key: K, value: V)
expect fun exit()
expect fun buildWebClient(): HttpClient
expect fun getListenedForClass(listener: EventListener<*>): KClass<*>

expect object Math {
    val PI: Double
    fun random(): Double
    fun toDegrees(radians: Double): Double
    fun nextInt(current: Int): Int
}

expect object Integer {
    fun parseInt(value: String?): Int
}