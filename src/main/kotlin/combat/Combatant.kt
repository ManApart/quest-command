package combat

import combat.battle.BattleAction
import core.gameState.Target
import core.gameState.body.BodyPart

import core.gameState.stat.HEALTH
import core.gameState.stat.WISDOM
import core.history.display

class Combatant(val target: Target) {
    private var actionPoints = 0
    var action: BattleAction? = null
    var blockBodyPart: BodyPart? = null
    var lastAttacked: Target? = null
    val blockedBodyParts: MutableList<BodyPart> = mutableListOf()

    fun tick() {
        if (action == null) {
            increaseActionPoints()
        } else {
            action!!.timeLeft--
        }
    }

    private fun increaseActionPoints() {
        actionPoints += target.soul.getCurrent(WISDOM, 1)
    }

    fun isActionReady(): Boolean {
        return action != null && action!!.timeLeft <= 0
    }

    fun canChooseAction(): Boolean {
        return actionPoints >= 100
    }

    fun resetStance() {
        blockedBodyParts.clear()
        blockBodyPart = null
    }

    fun chooseAction() {
        if (!target.isPlayer()) {
            display("${target.name} considers its choices.")
            target.ai.takeAction()
        }
        actionPoints = 0
    }

    fun status(): String {
        return "${target.name}: ${target.soul.getCurrent(HEALTH)}/${target.soul.getTotal(HEALTH)} hp, $actionPoints/100 ap, ${action?.javaClass?.simpleName ?: "None"}."
    }
}