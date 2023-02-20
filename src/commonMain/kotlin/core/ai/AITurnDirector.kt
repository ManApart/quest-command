package core.ai

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.thing.Thing
import time.gameTick.GameTickEvent

private const val turnLimit = 1000

suspend fun directAI() {
    val creatures = GameState.players.values.flatMap { it.thing.location.getLocation().getCreatures(it.thing) }.toSet().toList()

    //If only one creature, instantly fill their action points to avoid all the looping
    if (creatures.size == 1) {
        creatures.first().mind.ai.maxActionPoints()
    }

    if (creatures.isNotEmpty()) {
        var turns = 0
        var takeAnotherTurn = takeATurn(creatures)
        while (turns < turnLimit && takeAnotherTurn) {
            takeAnotherTurn = takeATurn(creatures)
            turns++
        }
        if (turns >= turnLimit) {
            println("Got stuck in a loop taking turns. This shouldn't happen!")
        }
    }

    //Don't let the player choose another action until their delayed action is complete
    val playerHasDelayedAction = creatures.any { it.isPlayer() && it.mind.ai.action != null }
    if (playerHasDelayedAction) {
        EventManager.postEvent(GameTickEvent())
    }

}

private suspend fun takeATurn(creatures: List<Thing>): Boolean {
    //Sort by action points and give players 1 extra point so they always come before npcs
    val allAIs = creatures.map { it.mind.ai }.sortedByDescending { it.getActionPoints() + if (it.creature.isPlayer()) 1 else 0 }
    allAIs.forEach { it.tick() }
    var executedEvent = false

    allAIs.forEach {
        val isPlayer = it.creature.isPlayer()
        if (!executedEvent || isPlayer) {
            if (it.isActionReady()) {
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
                executedEvent = true
            } else if (it.canChooseAction()) {
                it.creature.body.blockHelper.resetStance()
                it.chooseAction()
                executedEvent = true
            }
        }
    }

    return !executedEvent
}
