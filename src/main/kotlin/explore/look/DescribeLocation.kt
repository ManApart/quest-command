package explore.look

import core.Player
import core.history.displayToMe
import core.thing.perceivedBy
import core.thing.toThingString
import traveling.position.NO_VECTOR
import traveling.scope.getHeatLevel
import traveling.scope.getLightLevel

fun describeLocation(source: Player) {
    val pos = source.position
    if (pos == NO_VECTOR) {
        source.displayToMe("You are at ${source.location.name}")
    } else {
        source.displayToMe("You are at ${pos.x}, ${pos.y}, ${pos.z} of ${source.location.name}")
    }
    describePerceivedThings(source)
}

private fun describePerceivedThings(source: Player) {
    val things = source.thing.currentLocation().getThings().filterNot { it.isPlayer() }.toList().perceivedBy(source.thing)
    when {
        things.isEmpty() && source.location.getLocation().getLightLevel() < source.thing.getClarity() -> {
            source.displayToMe("It's too dark to see anything.")
        }
        things.isEmpty() -> {
            source.displayToMe("You don't see anything of use.")
        }
        else -> {
            source.displayToMe("You find yourself surrounded by ${things.toThingString()}.")
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

    describePerceivedThings(source)
}