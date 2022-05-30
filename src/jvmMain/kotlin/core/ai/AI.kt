package core.ai

import conversation.dialogue.DialogueEvent
import core.events.DelayedEvent
import core.properties.ACTION_POINTS
import core.thing.Thing
import status.stat.WISDOM

abstract class AI(val name: String) {
    lateinit var creature: Thing
    abstract fun hear(event: DialogueEvent)
    abstract fun takeAction()

    var aggroThing: Thing? = null
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
        creature.mind.forgetShortTermMemory()
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