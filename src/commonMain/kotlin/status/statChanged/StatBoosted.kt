package status.statChanged

import core.events.EventListener
import core.history.display
import core.utility.asSubjectPossessive
import core.utility.then
import kotlin.math.abs

class StatBoosted : EventListener<StatBoostEvent>() {

    override suspend fun shouldExecute(event: StatBoostEvent): Boolean {
        return event.amount != 0
    }

    override suspend fun execute(event: StatBoostEvent) {
        val change = (event.amount > 0).then("increases", "decreases")

        val soul = event.thing.soul
        soul.incStatMax(event.type, event.amount)

        val current = soul.getCurrent(event.type)
        val max = soul.getTotal(event.type)
        event.thing.display{
            val subject = event.thing.asSubjectPossessive(it)
            "${event.source} $change $subject max ${event.type} by ${abs(event.amount)} ($current/$max)."
        }
    }
}