package core.ai

import core.GameState
import core.events.EventListener
import core.events.EventManager
import time.gameTick.GameTickEvent

class AITurnDirector : EventListener<GameTickEvent>() {

    override fun execute(event: GameTickEvent) {
        val creatureAIs = GameState.player.location.getLocation().getCreatures().map { it.ai }

        val takeAnotherTurn = takeATurn(creatureAIs)
        if (takeAnotherTurn) {
            EventManager.postEvent(GameTickEvent())
        }

    }

    private fun takeATurn(creatureAIs: List<AI>): Boolean {
        creatureAIs.forEach { it.tick() }
        creatureAIs.forEach {
            if (it.isActionReady()) {
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
                return false
            } else if (it.canChooseAction()) {
                it.creature.body.blockHelper.resetStance()
                it.chooseAction()
                return false
            }
        }
        return true
    }

}