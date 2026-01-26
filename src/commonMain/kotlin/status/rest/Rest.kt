package status.rest

import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe
import core.utility.then
import explore.listen.addSoundEffect
import status.stat.Attributes.HEALTH
import status.stat.Attributes.STAMINA
import status.stat.LeveledStat
import status.statChanged.StatChangeEvent

class Rest : EventListener<RestEvent>() {

    override suspend fun complete(event: RestEvent) {
        if (!event.creature.isSafe()) {
            event.creature.displayToMe("You can't rest right now!")
        } else {
            rest(event)
        }
    }

    private fun rest(event: RestEvent) {
        val recoverableStats = event.creature.soul.getStats().filter { isRecoverable(it) }

        if (recoverableStats.isEmpty()) {
            val hours = (event.hoursRested != 1).then("${event.hoursRested} hours", "1 hour")
            event.creature.displayToMe("You rest for $hours but feel the same.")
        }

        recoverableStats.forEach {
            if (it.name == STAMINA) {
                EventManager.postEvent(StatChangeEvent(event.creature, "Resting", it.name, event.hoursRested * 10))
            } else {
                EventManager.postEvent(StatChangeEvent(event.creature, "Resting", it.name, event.hoursRested))
            }
        }
        event.creature.addSoundEffect("Resting", "slow, steady breathing", 2)
    }

    private fun isRecoverable(leveledStat: LeveledStat): Boolean {
        return leveledStat.name != HEALTH
                && leveledStat.current < leveledStat.max
    }
}