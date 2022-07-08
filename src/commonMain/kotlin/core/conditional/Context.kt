package core.conditional

import core.thing.Thing
import traveling.location.location.Location

typealias ContextData = Map<String, (Thing, Context) -> Any?>

@Suppress("UNCHECKED_CAST")
class Context(private val data: ContextData = mapOf()) {
    fun thing(name: String, source: Thing) = data[name]?.invoke(source, this) as Thing?
    fun things(name: String, source: Thing) = data[name]?.invoke(source, this) as List<Thing>?
    fun int(name: String, source: Thing) = data[name]?.invoke(source, this) as Int?
    fun loc(name: String, source: Thing) = data[name]?.invoke(source, this) as Location?
}