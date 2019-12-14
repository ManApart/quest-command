package status.statChanged

import core.events.EventListener
import core.GameState
import core.history.display
import core.utility.StringFormatter
import system.debug.DebugType
import kotlin.math.abs

class StatChanged : EventListener<StatChangeEvent>() {

    override fun shouldExecute(event: StatChangeEvent): Boolean {
        return event.amount != 0
    }

    override fun execute(event: StatChangeEvent) {
        val change = StringFormatter.format(event.amount > 0, "increases", "decreases")
        val soul = event.target.soul

        val oldVal = soul.getCurrent(event.statName)

        if (!GameState.properties.values.getBoolean(DebugType.STAT_CHANGES.propertyName)) {
            soul.incStat(event.statName, event.amount)
        }

        val subject = StringFormatter.getSubjectPossessive(event.target)
        val current = soul.getCurrent(event.statName)
        val max = soul.getTotal(event.statName)

        if (current != oldVal && !event.silent) {
            display("${event.sourceOfChange} $change $subject ${event.statName} by ${abs(event.amount)} ($current/$max).")
        }
    }

}