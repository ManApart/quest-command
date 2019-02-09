package status.rest

import core.events.EventListener
import core.gameState.stat.HEALTH
import core.gameState.stat.Stat
import status.statChanged.StatChangeEvent
import system.EventManager

class Rest : EventListener<RestEvent>() {

    override fun execute(event: RestEvent) {
        val recoverableStats = event.creature.soul.getStats().filter { isRecoverable(it) }

        recoverableStats.forEach {
            EventManager.postEvent(StatChangeEvent(event.creature, "Resting", it.name, event.hoursRested))
        }
    }

    private fun isRecoverable(stat: Stat): Boolean {
        return stat.name != HEALTH
                && stat.current < stat.max
    }
}