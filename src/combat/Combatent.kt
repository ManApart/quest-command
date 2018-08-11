package combat

import core.gameState.Creature
import core.gameState.GameState

class Combatent(val creature: Creature) {
    private var actionPoints = 0

    fun increaseActionPoints() {
        actionPoints++
    }

    fun canAct() : Boolean {
        return actionPoints >= 100
    }

    fun act() {
        if (GameState.player.creature != creature){
            println("${creature.name} is too dumb to do anything!")
        }
    }

}