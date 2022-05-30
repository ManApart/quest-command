package core.utility

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/*
 * Taken from https://stackoverflow.com/questions/47947841/kotlin-var-lazy-init
 * This allows us to lazily initialize vars, which lets us lazily initialize our managers and save on start all at once cost.
 * Start up all at once cost means a longer initial load time for weaker devices, like android phones.
 * This change, coupled with turning on startup logs should reduce load time and make it feel snappier
 */
class LazyMutable<T>(val initializer: () -> T) : ReadWriteProperty<Any?, T> {
    private object UNINITIALIZED_VALUE
    private var prop: Any? = UNINITIALIZED_VALUE

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return if (prop == UNINITIALIZED_VALUE) {
            synchronized(this) {
                return if (prop == UNINITIALIZED_VALUE) initializer().also { prop = it } else prop as T
            }
        } else prop as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        synchronized(this) {
            prop = value
        }
    }
}

fun <T> lazyM(initializer: () -> T) = LazyMutable(initializer)