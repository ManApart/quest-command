package core.ai

import conversation.dialogue.DialogueEvent
import core.events.DelayedEvent
import core.properties.ACTION_POINTS
import core.target.Target
import status.stat.WISDOM

abstract class AI(val name: String, val creature: Target) {
    abstract fun hear(event: DialogueEvent)
    abstract fun takeAction()

    var aggroTarget: Target? = null
    var action: DelayedEvent? = null

    private var actionPoints = 0

    fun tick() {
        if (action == null) {
            increaseActionPoints()
        } else {
            action!!.timeLeft--
        }
    }

    private fun increaseActionPoints() {
        val soulPoints = creature.properties.values.getInt(ACTION_POINTS, 0)
        actionPoints += creature.soul.getCurrent(WISDOM, 1) + soulPoints
    }

    fun isActionReady(): Boolean {
        return action != null && action!!.timeLeft <= 0
    }

    fun canChooseAction(): Boolean {
        return action == null && actionPoints >= 100
    }

    fun chooseAction() {
        if (!creature.isPlayer()) {
            takeAction()
        }
        actionPoints = 0
    }

    fun maxActionPoints() {
        actionPoints = 100
    }

    fun getActionPoints() : Int {
        return actionPoints
    }


}