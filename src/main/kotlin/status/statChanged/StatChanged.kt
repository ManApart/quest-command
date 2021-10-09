package status.statChanged

import core.GameState
import core.events.EventListener
import core.history.display
import core.utility.then
import core.utility.asSubjectPossessive
import system.debug.DebugType
import kotlin.math.abs

class StatChanged : EventListener<StatChangeEvent>() {

    override fun shouldExecute(event: StatChangeEvent): Boolean {
        return event.amount != 0
    }

    override fun execute(event: StatChangeEvent) {
        val change = (event.amount > 0).then("increases", "decreases")
        val soul = event.target.soul

        val oldVal = soul.getCurrent(event.statName)

        if (!GameState.getDebugBoolean(DebugType.STAT_CHANGES)) {
            soul.incStat(event.statName, event.amount)
        }

        val current = soul.getCurrent(event.statName)
        val max = soul.getTotal(event.statName)

        if (current != oldVal && !event.silent) {
            event.target.display {
                val subject = event.target.asSubjectPossessive(it)
                "${event.sourceOfChange} $change $subject ${event.statName} by ${abs(event.amount)} ($current/$max)."
            }
        }
    }
}