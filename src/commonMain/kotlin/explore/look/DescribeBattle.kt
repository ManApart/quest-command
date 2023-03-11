package explore.look

import core.Player
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import status.stat.HEALTH

//TODO - only display to me?
suspend fun describeBattle(source: Player) {
    val creatures = source.location.getLocation().getCreatures(source.thing)
    creatures.filter { it !== source.thing }.forEach {
        it.display("${it.getDisplayName()} is ${source.position.getDistance(it.position)} away from ${source.thing.getDisplayName()}.")
    }

//    if (!CommandParser.isPlayersTurn()) {
//        source.display("It is ${CommandParser.commandSource}'s turn.")
//    }

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
        ""
//            //TODO timing
//        when {
//            it.mind.ai.action != null -> "${it.name} is preforming an action with ${it.mind.ai.action!!.timeLeft} time left."
//            else -> "${it.name} is getting ready to make a choice."
//        }
    }.filter { it.isNotBlank() }.joinToString("\n")

    if (combatantString.isNotBlank()) {
        source.displayToMe(combatantString)
    }
}
