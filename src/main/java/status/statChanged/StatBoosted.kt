package status.statChanged

import core.events.EventListener
import core.gameState.getSoul
import core.gameState.hasSoul
import core.utility.StringFormatter

class StatBoosted : EventListener<StatBoostEvent>() {

    override fun shouldExecute(event: StatBoostEvent): Boolean {
        return event.amount != 0 && hasSoul(event.target)
    }

    override fun execute(event: StatBoostEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val subject = StringFormatter.getSubjectPossessive(event.target)
        val soul = getSoul(event.target)!!
        soul.incStatMax(event.type, event.amount)

        val current = soul.getCurrent(event.type)
        val max = soul.getTotal(event.type)
        println("${event.source} $change $subject max ${event.type} by ${Math.abs(event.amount)} ($current/$max).")
    }
}