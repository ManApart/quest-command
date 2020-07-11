package status.rest

import core.GameState
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.StringFormatter.format
import status.stat.HEALTH
import status.stat.LeveledStat
import status.stat.STAMINA
import status.statChanged.StatChangeEvent

class Rest : EventListener<RestEvent>() {

    override fun execute(event: RestEvent) {
        if (!GameState.player.isSafe()) {
            display("You can't rest right now!")
        } else {
            rest(event)
        }
    }

    private fun rest(event: RestEvent) {
        val recoverableStats = event.creature.soul.getStats().filter { isRecoverable(it) }

        if (recoverableStats.isEmpty()) {
            val hours = format(event.hoursRested != 1, "${event.hoursRested} hours", "1 hour")
            display("You rest for $hours but feel the same.")
        }

        recoverableStats.forEach {
            if (it.name == STAMINA) {
                EventManager.postEvent(StatChangeEvent(event.creature, "Resting", it.name, event.hoursRested * 10))
            } else {
                EventManager.postEvent(StatChangeEvent(event.creature, "Resting", it.name, event.hoursRested))
            }
        }
    }

    private fun isRecoverable(leveledStat: LeveledStat): Boolean {
        return leveledStat.name != HEALTH
                && leveledStat.current < leveledStat.max
    }
}