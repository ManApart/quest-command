package interact.actions

import core.events.EventListener
import core.history.display
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject
import interact.UseEvent

class OutOfReachUse : EventListener<UseEvent>() {

    override fun getPriorityRank(): Int {
        return -1
    }

    override fun shouldExecute(event: UseEvent): Boolean {
        return !event.used.isWithinRangeOf(event.source) || !event.target.isWithinRangeOf(event.source)
    }

    override fun execute(event: UseEvent) {
        if (!event.used.isWithinRangeOf(event.source)) {
            display(getSubject(event.source) + " " + getIsAre(event.source) + " too far away to use ${event.used}.")
        }
        if (!event.target.isWithinRangeOf(event.source)) {
            display(getSubject(event.source) + " " + getIsAre(event.source) + " too far away to use ${event.used} on ${event.target}.")
        }
    }

}