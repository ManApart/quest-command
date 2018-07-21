package status

import core.events.EventListener
import core.utility.StringFormatter
import status.StatChangeEvent

class StatChanged : EventListener<StatChangeEvent>() {

    override fun shouldExecute(event: StatChangeEvent): Boolean {
        return event.amount != 0
    }

    override fun execute(event: StatChangeEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val subject = StringFormatter.getSubjectPossessive(event.target)
        event.target.soul.incStat(event.target, event.type, event.amount)

        val current = event.target.soul.getCurrent(event.type)
        val max = event.target.soul.getTotal(event.type)
        println("${event.source} $change $subject ${event.type} by ${Math.abs(event.amount)} ($current/$max).")
    }
}