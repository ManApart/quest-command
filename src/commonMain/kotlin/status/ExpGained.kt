package status

import core.events.EventListener
import core.history.displayToMe

class ExpGained : EventListener<ExpGainedEvent>() {
    override suspend fun shouldExecute(event: ExpGainedEvent): Boolean {
        return event.creature.isPlayer()
    }

    override suspend fun complete(event: ExpGainedEvent) {
        val stat = event.creature.soul.getStatOrNull(event.stat)
        if (stat != null && event.amount > 0) {
            if (stat.expExponential > 1) {
                event.creature.displayToMe("You gained ${event.amount} exp in ${stat.name}")
            }
            stat.addEXP(event.amount)
        }
    }
}