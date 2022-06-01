package core.utility

import core.events.EventListener
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes
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

actual fun buildWebClient(): HttpClient {
    return HttpClient(CIO) { install(ContentNegotiation) { json() } }
}

actual fun getListenedForClass(listener: EventListener<*>): KClass<*> {
    return listener::class.allSupertypes.first { it.classifier == EventListener::class }.arguments.first().type!!.classifier as KClass<*>
}

actual object Math {
    actual val PI = java.lang.Math.PI

    actual fun random(): Double {
        return java.lang.Math.random()
    }

    actual fun toDegrees(radians: Double): Double {
        return java.lang.Math.toDegrees(radians)
    }

    actual fun nextInt(current: Int): Int {
        return Random().nextInt(current)
    }
}

actual object Integer {
    actual fun parseInt(value: String?): Int {
        return java.lang.Integer.parseInt(value)
    }
}