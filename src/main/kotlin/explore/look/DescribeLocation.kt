package explore.look

import core.Player
import core.history.displayToMe
import core.thing.perceivedBy
import core.thing.toThingString
import traveling.location.location.Location
import traveling.position.NO_VECTOR
import traveling.scope.getHeatLevel
import traveling.scope.getLightLevel

fun describeLocation(source: Player, location: Location) {
    describePosition(source, location)
    describePerceivedThings(source, location)
}

private fun describePosition(source: Player, location: Location) {
    if (source.thing.currentLocation() == location) {
        val pos = source.position
        if (pos == NO_VECTOR) {
            source.displayToMe("You are at ${location.name}.")
        } else {
            source.displayToMe("You are at ${pos.x}, ${pos.y}, ${pos.z} of ${location.name}.")
        }
    } else {
        source.displayToMe("You look at ${location.name}.")
    }
}

private fun describePerceivedThings(source: Player, location: Location) {
    val things = location.getThings().filterNot { it.isPlayer() }.toList().perceivedBy(source.thing)
    when {
        things.isEmpty() && location.getLightLevel() < source.thing.getClarity() -> {
            source.displayToMe("It's too dark to see anything.")
        }
        things.isEmpty() -> {
            source.displayToMe("You don't see anything of use.")
        }
        else -> {
            source.displayToMe("It contains ${things.toThingString()}.")
        }
    }
}

fun describeLocationDetailed(source: Player) {
    val pos = source.position
    val locationRecipe = source.location.getLocationRecipe()
    val location = source.thing.currentLocation()
    if (pos == NO_VECTOR) {
        source.displayToMe("You are at ${source.location.name}")
    } else {
        source.displayToMe("You are at ${pos.x}, ${pos.y}, ${pos.z} of ${source.location.name}")
    }
    source.displayToMe(locationRecipe.getDescription())
    source.displayToMe(location.weather.description)
    val light = location.getLightLevel()
    val heat = getHeatLevel(location)
    source.displayToMe("It is $light light and $heat hot.")

    describePerceivedThings(source, source.thing.currentLocation())
}