package explore.look

import core.Player
import core.history.display
import core.history.displayToMe
import core.history.displayUpdate
import core.history.displayUpdateEnd
import core.thing.Thing
import status.stat.HEALTH

fun describeBattle(source: Player) {
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
    return "${thing.name}: ${thing.soul.getCurrent(HEALTH)}/${thing.soul.getTotal(HEALTH)} HP, ${thing.ai.getActionPoints()}/100 AP, ${thing.ai.action?.javaClass?.simpleName ?: "None"}."
}

private fun printTurnStatus(source: Player, creatures: List<Thing>) {
    val combatantString = creatures.map {
        when {
            it.ai.isActionReady() -> ""
            it.ai.getActionPoints() == 0 -> ""
            it.ai.canChooseAction() -> "${it.name} is making a choice"
            it.ai.action != null -> "${it.name} is preforming an action with ${it.ai.action!!.timeLeft} time left"
            else -> "${it.name} is getting ready to make a choice: ${it.ai.getActionPoints()}/100"
        }
    }.filter { it.isNotBlank() }.joinToString("\n")

    if (combatantString.isNotBlank()) {
        source.displayToMe(combatantString)
    }
}

fun printUpdatingStatus(creatures: List<Thing>) {
    val combatantString = creatures.joinToString("\t\t") {
        val points = it.ai.getActionPoints()
        val timeLeft = it.ai.action?.timeLeft ?: 0
        "${it.getDisplayName()}: $points AP, $timeLeft action time left"
    }
    displayUpdate(combatantString)
}

fun printUpdatingStatusEnd(creatures: List<Thing>) {
    val combatantString = creatures.joinToString("\t\t") {
        val points = it.ai.getActionPoints()
        val timeLeft = it.ai.action?.timeLeft ?: 0
        "${it.getDisplayName()}: $points AP, $timeLeft action time left"
    }
    displayUpdateEnd(combatantString)
}