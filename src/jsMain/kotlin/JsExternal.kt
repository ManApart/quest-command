@file:Suppress("UNCHECKED_CAST")

import FakeSync.promise
import kotlin.js.Promise

@JsModule("localforage")
@JsNonModule
external object LocalForage {
    fun setItem(key: String, value: Any): Promise<*>
    fun getItem(key: String): Promise<Any?>
    fun keys(): Promise<Any?>
    fun config(config: LocalForageConfig)
}

data class LocalForageConfig(val name: String)

suspend fun <T> getForage(key: String) : T? {
    return promise {
        LocalForage.getItem(key) as Promise<T?>
    }
}

suspend fun getForageKeys() : List<String> {
    return promise {
        LocalForage.keys() as Promise<List<String>>
    }!!.toString().split(",")
}

suspend fun setForage(key: String, value: Any) {
    promise {
        LocalForage.setItem(key, value)
    }
}
