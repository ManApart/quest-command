package status.statChanged

import core.events.EventListener
import core.gameState.Target
import core.history.display
import core.utility.StringFormatter
import kotlin.math.abs

class StatChanged : EventListener<StatChangeEvent>() {

    override fun shouldExecute(event: StatChangeEvent): Boolean {
        return event.amount != 0
    }

    override fun execute(event: StatChangeEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val soul = event.target.soul

        soul.incStat(event.statName, event.amount)

        val subject = StringFormatter.getSubjectPossessive(event.target)
        val current = soul.getCurrent(event.statName)
        val max = soul.getTotal(event.statName)
        if (!event.silent) {
            display("${event.sourceOfChange} $change $subject ${event.statName} by ${abs(event.amount)} ($current/$max).")
        }
    }

}