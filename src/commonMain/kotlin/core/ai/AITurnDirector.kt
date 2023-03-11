package core.ai

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.thing.Thing
import core.utility.map
import time.gameTick.GameTickEvent

suspend fun directAI() {
    val creatures = GameState.players.values.flatMap { it.thing.location.getLocation().getCreatures(it.thing) }.toSet()

    creatures.map { it.mind.ai }.filter { it.action == null }.forEach {
        it.creature.body.blockHelper.resetStance()
        it.chooseAction()
    }

}
