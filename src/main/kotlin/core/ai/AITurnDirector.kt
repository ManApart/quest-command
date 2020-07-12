package core.ai

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.target.Target
import explore.look.printUpdatingStatusEnd

class AITurnDirector : EventListener<AIUpdateTick>() {

    override fun execute(event: AIUpdateTick) {
        val creatures = GameState.player.location.getLocation().getCreatures()

        //If only one creature, instantly fill their action points to avoid all the looping
        if (creatures.size == 1) {
            creatures.first().ai.maxActionPoints()
        }

        if (creatures.isNotEmpty()) {
            val takeAnotherTurn = takeATurn(creatures)
            if (takeAnotherTurn) {
                EventManager.postEvent(AIUpdateTick())
            }
        }

    }

    private fun takeATurn(creatures: List<Target>): Boolean {
        val creatureAIs = creatures.map { it.ai }
        creatureAIs.forEach { it.tick() }
        creatureAIs.forEach {
            //Make this only if some verbosity level is set?
            if (it.isActionReady()) {
                if (it.aggroTarget != null) {
                    printUpdatingStatusEnd(creatures)
                }
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
                return false
            } else if (it.canChooseAction()) {
                if (it.aggroTarget != null) {
                    printUpdatingStatusEnd(creatures)
                }
                it.creature.body.blockHelper.resetStance()
                it.chooseAction()
                return false
            }
        }
        return true
    }

}