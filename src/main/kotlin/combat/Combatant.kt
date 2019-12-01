package combat

import combat.battle.BattleAction
import core.gameState.ACTION_POINTS
import core.gameState.Target
import core.gameState.body.BodyPart
import core.gameState.stat.HEALTH
import core.gameState.stat.WISDOM

class Combatant(val target: Target) {
    private var actionPoints = 0
    var action: BattleAction? = null
    var blockBodyPart: BodyPart? = null
    var lastAttacked: Target? = null
    val blockedBodyParts: MutableList<BodyPart> = mutableListOf()

    override fun toString(): String {
        return "${target.name}: $actionPoints"
    }

    fun tick() {
        if (action == null) {
            increaseActionPoints()
        } else {
            action!!.timeLeft--
        }
    }

    private fun increaseActionPoints() {
        val soulPoints = target.properties.values.getInt(ACTION_POINTS, 0)
        actionPoints += target.soul.getCurrent(WISDOM, 1) + soulPoints
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
//            display("${target.name} considers its choices.")
            target.ai.takeAction()
        }
        actionPoints = 0
    }

    fun status(): String {
        return "${target.name}: ${target.soul.getCurrent(HEALTH)}/${target.soul.getTotal(HEALTH)} hp, $actionPoints/100 ap, ${action?.javaClass?.simpleName ?: "None"}."
    }

    fun getActionPoints() : Int {
        return actionPoints
    }

}