package use.interaction.nothing

import core.events.Event
import core.target.Target

class NothingEvent(val source: Target, private val hoursWaited: Int = 1) : Event {
    override fun gameTicks(): Int = hoursWaited
    override fun isExecutableByAI(): Boolean = true
}