package combat

import core.gameState.Creature
import core.gameState.GameState
import core.history.display

class Combatant(val creature: Creature) {
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
                display("${creature.name} considers its choices.")
                creature.ai.takeAction()
            } else {
                display("${creature.name} does nothing!")
            }
        }
        actionPoints = 0
    }

    fun isPlayer() : Boolean {
        return creature == GameState.player.creature
    }

}