package combat

import combat.battle.BattleAction
import core.gameState.Creature
import core.gameState.isPlayer
import core.history.display

class Combatant(val creature: Creature) {
    private var actionPoints = 0
    var action: BattleAction? = null

    fun tick() {
        if (action == null){
            increaseActionPoints()
        } else {
            action!!.timeLeft --
        }
    }

    private fun increaseActionPoints() {
        actionPoints++
    }

    fun isActionReady() : Boolean {
        return action != null && action!!.timeLeft <= 0
    }

    fun canChooseAction() : Boolean {
        return actionPoints >= 100
    }

    fun chooseAction() {
        if (!creature.isPlayer()){
            if (creature.ai != null){
                display("${creature.name} considers its choices.")
                creature.ai.takeAction()
            } else {
                display("${creature.name} does nothing!")
            }
        }
        actionPoints = 0
    }

}