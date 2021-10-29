package core.commands

import core.thing.Thing
import traveling.location.location.Location
import traveling.location.network.NOWHERE_NODE

class ThingOrLocation(val thing: Thing?, val location: Location?) {
    constructor(thing: Thing) : this(thing, null)
    constructor(location: Location) : this(null, location)
}

fun findThing(source: Thing, nameOrPath: String): ThingOrLocation? {
    val current = ThingOrLocation(source.currentLocation())
    val path = nameOrPath.split(":")

    return findThing(current, path)
}

fun findThing(current: ThingOrLocation, path: List<String>): ThingOrLocation? {
    if (path.isEmpty()) return current
    val nextName = path.first()

    return when {
        current.location != null -> findByLocation(current.location, nextName, path)
        current.thing != null -> findByThing(current.thing, nextName, path)
        else -> null
    }

}

private fun findByThing(
    thing: Thing,
    nextName: String,
    path: List<String>
): ThingOrLocation? {
    val location = thing.body.layout.findLocation(nextName)
    return if (location == NOWHERE_NODE) null
    else {
        findThing(ThingOrLocation(location.getLocation()), path.subList(1, path.size))
    }
}

private fun findByLocation(
    location: Location,
    nextName: String,
    path: List<String>
): ThingOrLocation? {
    val thing = location.getThings().getOrNull(nextName)
    return if (thing == null) null
    else {
        findThing(ThingOrLocation(thing), path.subList(1, path.size))
    }
}