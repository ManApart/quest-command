package interact.actions

import core.events.EventListener
import core.gameState.GameState
import core.gameState.Stat
import core.utility.StringFormatter
import interact.UseEvent
import status.StatChangeEvent
import status.StatMinnedEvent

class StatChanged : EventListener<StatChangeEvent>() {
    override fun execute(event: StatChangeEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val subject = StringFormatter.getSubject(event.target)
        println("${event.source} $change $subject ${event.type} by ${Math.abs(event.amount)}")
        event.target.soul.incStat(event.target, event.type, event.amount)

        val current = event.target.soul.getCurrent(event.type)
        val max = event.target.soul.getTotal(event.type)
        println("$subject ${event.type} $change to $current/$max")
    }
}