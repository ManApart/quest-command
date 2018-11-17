package status.statChanged

import core.events.EventListener
import core.gameState.getSoul
import core.gameState.hasSoul
import core.utility.StringFormatter

class StatChanged : EventListener<StatChangeEvent>() {
    private val hiddenStats = listOf("burnHealth", "chopHealth")

    override fun shouldExecute(event: StatChangeEvent): Boolean {
        return event.amount != 0 && hasSoul(event.target)
    }

    override fun execute(event: StatChangeEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val soul = getSoul(event.target)!!

        soul.incStat(event.type, event.amount)

        if (!hiddenStats.contains(event.type)) {
            val subject = StringFormatter.getSubjectPossessive(event.target)
            val current = soul.getCurrent(event.type)
            val max = soul.getTotal(event.type)
            println("${event.source} $change $subject ${event.type} by ${Math.abs(event.amount)} ($current/$max).")
        }
    }
}