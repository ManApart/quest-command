package status.statChanged

import core.events.EventListener
import core.gameState.getSoul
import core.gameState.hasSoul
import core.history.display
import core.utility.StringFormatter

class StatChanged : EventListener<StatChangeEvent>() {
    private val hiddenStats = listOf("burnHealth", "chopHealth")

    override fun shouldExecute(event: StatChangeEvent): Boolean {
        return event.amount != 0 && event.target.hasSoul()
    }

    override fun execute(event: StatChangeEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val soul = event.target.getSoul()!!

        soul.incStat(event.type, event.amount)

        if (!hiddenStats.contains(event.type)) {
            val subject = StringFormatter.getSubjectPossessive(event.target)
            val current = soul.getCurrent(event.type)
            val max = soul.getTotal(event.type)
            display("${event.source} $change $subject ${event.type} by ${Math.abs(event.amount)} ($current/$max).")
        }
    }
}