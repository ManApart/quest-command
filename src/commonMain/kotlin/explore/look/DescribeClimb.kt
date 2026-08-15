package explore.look

import core.Player
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import core.utility.wrapNonEmpty

suspend fun describeClimbJourney(source: Player, detailed: Boolean = false) {
    val location = source.thing.location
    val distance = getDistance(source.thing).wrapNonEmpty("", " ")
    val climbing = source.thing.climbThing?.let { ", climbing the $it" } ?: ""
    source.displayToMe("You are ${distance}above the ground${climbing} at ${location.name}.")
    if (detailed) source.displayToMe("${location.name} is made of ${location.getLocation().material.name}.")
    source.displayToOthers("${source.name} looks around.")
}

private fun getDistance(creature: Thing): String {
    return (creature.climbThing?.getHeight() ?: creature.position.z).toString()
}
