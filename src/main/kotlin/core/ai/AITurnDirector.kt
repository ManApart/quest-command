package core.ai

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.thing.Thing
import explore.look.printUpdatingStatusEnd

private const val turnLimit = 1000

class AITurnDirector : EventListener<AIUpdateTick>() {

    override fun execute(event: AIUpdateTick) {
        val creatures = GameState.players.values.flatMap { it.thing.location.getLocation().getCreatures(it.thing) }.toSet().toList()

        //If only one creature, instantly fill their action points to avoid all the looping
        if (creatures.size == 1) {
            creatures.first().ai.maxActionPoints()
        }

        if (creatures.isNotEmpty()) {
            var turns = 0
            var takeAnotherTurn = takeATurn(creatures)
            while (turns < turnLimit && takeAnotherTurn){
                takeAnotherTurn = takeATurn(creatures)
                turns++
            }
            if (turns >= turnLimit){
                println("Got stuck in a loop taking turns. This shouldn't happen!")
            }
        }

    }

    private fun takeATurn(creatures: List<Thing>): Boolean {
        val multiplayer = creatures.count { it.isPlayer() } > 1
        return if (multiplayer){
            takeATurnMultiPlayer(creatures)
        } else takeATurnSinglePlayer(creatures)
    }

    private fun takeATurnSinglePlayer(creatures: List<Thing>): Boolean {
        val creatureAIs = creatures.map { it.ai }
        creatureAIs.forEach { it.tick() }
        creatureAIs.sortedByDescending { it.getActionPoints() }.forEach {
            //Make this only if some verbosity level is set?
            if (it.isActionReady()) {
                if (it.aggroThing != null) {
                    printUpdatingStatusEnd(creatures)
                }
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
                return false
            } else if (it.canChooseAction()) {
                if (it.aggroThing != null) {
                    printUpdatingStatusEnd(creatures)
                }
                it.creature.body.blockHelper.resetStance()
                it.chooseAction()
                return false
            }
        }
        return true
    }
    private fun takeATurnMultiPlayer(creatures: List<Thing>): Boolean {
        val allAIs = creatures.map { it.ai }.sortedByDescending { it.getActionPoints() }
        val (playerAIs, npcAIs) = allAIs.partition { it.creature.isPlayer() }
        allAIs.forEach { it.tick() }

        var needsAnotherTurn = true

        playerAIs.forEach {
            if (it.isActionReady()) {
                if (it.aggroThing != null) {
                    printUpdatingStatusEnd(creatures)
                }
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
                needsAnotherTurn = false
            } else if (it.canChooseAction()) {
                if (it.aggroThing != null) {
                    printUpdatingStatusEnd(creatures)
                }
                it.creature.body.blockHelper.resetStance()
            }
        }

        if (!needsAnotherTurn) return false

        npcAIs.forEach {
            if (it.isActionReady()) {
                if (it.aggroThing != null) {
                    printUpdatingStatusEnd(creatures)
                }
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
                return false
            } else if (it.canChooseAction()) {
                if (it.aggroThing != null) {
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