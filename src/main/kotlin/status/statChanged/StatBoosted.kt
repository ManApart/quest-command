package status.statChanged

import core.events.EventListener
import core.history.display
import core.utility.then
import core.utility.asSubjectPossessive
import kotlin.math.abs

class StatBoosted : EventListener<StatBoostEvent>() {

    override fun shouldExecute(event: StatBoostEvent): Boolean {
        return event.amount != 0
    }

    override fun execute(event: StatBoostEvent) {
        val change = (event.amount > 0).then("increases", "decreases")

        val soul = event.target.soul
        soul.incStatMax(event.type, event.amount)

        val current = soul.getCurrent(event.type)
        val max = soul.getTotal(event.type)
        event.target.display{
            val subject = event.target.asSubjectPossessive(it)
            "${event.source} $change $subject max ${event.type} by ${abs(event.amount)} ($current/$max)."
        }
    }
}