package explore.look

import core.Player
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import status.stat.Attributes.HEALTH

//TODO - only display to me?
suspend fun describeBattle(source: Player) {
    val creatures = source.location.getLocation().getCreatures(source.thing)
    creatures.filter { it !== source.thing }.forEach {
        it.display("${it.getDisplayName()} is ${source.position.getDistance(it.position)} away from ${source.thing.getDisplayName()}.")
    }

    creatures.forEach {
        it.display("\t${status(it)}")
    }
    printTurnStatus(source, creatures)
}

private fun status(thing: Thing): String {
    val actionName = thing.mind.ai.actions.firstOrNull()?.let { it::class.simpleName } ?: "None"
    return "${thing.name}: ${thing.soul.getCurrent(HEALTH)}/${thing.soul.getTotal(HEALTH)} HP, $actionName."
}

private fun printTurnStatus(source: Player, creatures: List<Thing>) {
    val combatantString = creatures.map {
        when {
            it.mind.ai.actions.isNotEmpty()-> "${it.name} is preforming an action with ${it.mind.ai.actions.minOf { action -> action.timeLeft }} time left."
            else -> "${it.name} is getting ready to make a choice."
        }
    }.filter { it.isNotBlank() }.joinToString("\n")

    if (combatantString.isNotBlank()) {
        source.displayToMe(combatantString)
    }
}
