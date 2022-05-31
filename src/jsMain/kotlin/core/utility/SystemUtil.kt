package core.utility

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

actual fun currentTime(): Long {
    throw NotImplementedError()
}

actual fun <K : Comparable<K>, V> Map<out K, V>.toSortedMap(): Map<K, V> {
    throw NotImplementedError()
}

actual fun <K,V> MutableMap<K,V>.putAbsent(key: K, value: V) {
    throw NotImplementedError()
}

actual fun exit(){
    println("Exited")
}

actual fun buildWebClient() : HttpClient {
    TODO("Not yet implemented")
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

    actual fun nextInt(current: Int): Int {
        TODO("Not yet implemented")
    }
}

object Integer {
    actual fun parseInt(value: String?): Int {
        throw NotImplementedError()
    }
}