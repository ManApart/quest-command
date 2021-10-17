package explore.look

import core.history.displayToMe
import core.thing.Thing
import core.thing.thingsToString
import traveling.position.NO_VECTOR
import traveling.scope.getHeatLevel
import traveling.scope.getLightLevel

fun describeLocation(source: Thing) {
    val pos = source.position
    if (pos == NO_VECTOR) {
        source.displayToMe("You are at ${source.location.name}")
    } else {
        source.displayToMe("You are at ${pos.x}, ${pos.y}, ${pos.z} of ${source.location.name}")
    }
    if (source.currentLocation().getThings().size > 1) {
        val thingList = thingsToString(source.currentLocation().getThings().filterNot { it.isPlayer() })
        source.displayToMe("You find yourself surrounded by $thingList.")
    } else {
        source.displayToMe("You don't see anything of use.")
    }
}

fun describeLocationDetailed(source: Thing) {
    val pos = source.position
    val locationRecipe = source.location.getLocationRecipe()
    val location = source.currentLocation()
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
    if (location.getThings().size > 1) {
        val thingList = thingsToString(location.getThings().filterNot { it.isPlayer() })
        source.displayToMe("You find yourself surrounded by $thingList.")
    } else {
        source.displayToMe("You don't see anything of use.")
    }
}