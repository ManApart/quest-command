package status.statChanged

import core.events.EventListener
import core.utility.StringFormatter

class StatBoosted : EventListener<StatBoostEvent>() {

    override fun shouldExecute(event: StatBoostEvent): Boolean {
        return event.amount != 0
    }

    override fun execute(event: StatBoostEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val subject = StringFormatter.getSubjectPossessive(event.target)
        event.target.soul.incStatMax(event.type, event.amount)

        val current = event.target.soul.getCurrent(event.type)
        val max = event.target.soul.getTotal(event.type)
        println("${event.source} $change $subject max ${event.type} by ${Math.abs(event.amount)} ($current/$max).")
    }
}