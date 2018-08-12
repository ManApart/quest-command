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
            if (creature.ai != null){
                println("${creature.name} considers its choices.")
                creature.ai.takeAction()
            } else {
                println("${creature.name} does nothing!")
            }
        }
        actionPoints = 0
    }

    fun isPlayer() : Boolean {
        return creature == GameState.player.creature
    }

}