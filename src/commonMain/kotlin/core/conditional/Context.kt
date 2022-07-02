package core.conditional

import core.thing.Thing
import traveling.location.location.Location

typealias ContextData = Map<String, (Thing, Context) -> Any?>

@Suppress("UNCHECKED_CAST")
class Context(private val data: ContextData = mapOf()) {
    fun thing(name: String) = data[name] as Thing?
    fun things(name: String) = data[name] as List<Thing>?
    fun int(name: String) = data[name] as Int?
    fun loc(name: String) = data[name] as Location?
}