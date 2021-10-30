package explore.look

import core.Player
import core.history.displayToMe
import core.thing.perceivedBy
import core.thing.toThingString
import traveling.location.location.Location
import traveling.location.weather.DEFAULT_WEATHER
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
    val allThings = location.getThings()
    val things = allThings.filterNot { it.isPlayer() }.toList().perceivedBy(source.thing)
    when {
        things.isEmpty() && allThings.isNotEmpty() && location.getLightLevel() < source.thing.getClarity() -> {
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

fun describeLocationDetailed(source: Player, location: Location) {
    val locationRecipe = location.locationNode.getLocationRecipe()
    describePosition(source, location)

    if (locationRecipe.getDescription().isNotBlank()) source.displayToMe(locationRecipe.getDescription())

    if (location.weather != DEFAULT_WEATHER) source.displayToMe(location.weather.description)

    val light = location.getLightLevel()
    val heat = getHeatLevel(location)
    if (light != 0 || heat != 0) source.displayToMe("It is $light light and $heat hot.")

    describePerceivedThings(source, location)
}