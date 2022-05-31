package core.utility

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

expect class LazyMutable<T>(initializer: () -> T) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
}

fun <T> lazyM(initializer: () -> T) = LazyMutable(initializer)