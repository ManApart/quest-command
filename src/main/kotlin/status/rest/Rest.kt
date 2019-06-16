package status.rest

import core.events.EventListener
import core.gameState.stat.HEALTH
import core.gameState.stat.Stat
import core.history.display
import core.utility.StringFormatter.format
import status.statChanged.StatChangeEvent
import system.EventManager

class Rest : EventListener<RestEvent>() {

    override fun execute(event: RestEvent) {
        val recoverableStats = event.creature.soul.getStats().filter { isRecoverable(it) }

        if (recoverableStats.isEmpty()){
            val hours = format(event.hoursRested != 1, "${event.hoursRested} hours", "1 hour")
            display("You rest for $hours but feel the same.")
        }

        recoverableStats.forEach {
            EventManager.postEvent(StatChangeEvent(event.creature, "Resting", it.name, event.hoursRested))
        }
    }

    private fun isRecoverable(stat: Stat): Boolean {
        return stat.name != HEALTH
                && stat.current < stat.max
    }
}