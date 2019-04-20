package combat

import combat.battle.BattleAction
import combat.battle.position.TargetPosition
import core.gameState.Target
import core.gameState.body.BodyPart

import core.gameState.stat.HEALTH
import core.gameState.stat.WISDOM
import core.history.display

class Combatant(val creature: Target) {
    private var actionPoints = 0
    var action: BattleAction? = null
    var position = TargetPosition()
    var blockPosition: TargetPosition? = null
    var blockBodyPart: BodyPart? = null

    fun tick() {
        if (action == null){
            increaseActionPoints()
        } else {
            action!!.timeLeft --
        }
    }

    private fun increaseActionPoints() {
        actionPoints += creature.soul.getCurrent(WISDOM, 1)
    }

    fun isActionReady() : Boolean {
        return action != null && action!!.timeLeft <= 0
    }

    fun canChooseAction() : Boolean {
        return actionPoints >= 100
    }

    fun resetStance() {
        position = TargetPosition()
        blockPosition = null
        blockBodyPart = null
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

    fun status() : String {
        return "${creature.name}: ${creature.soul.getCurrent(HEALTH)}/${creature.soul.getTotal(HEALTH)} hp, $actionPoints/100 ap, ${action?.javaClass?.simpleName ?: "None"}."
    }
}