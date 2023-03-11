package core.events

import core.thing.Thing

interface TemporalEvent : Event {
    val source: Thing
    var timeLeft: Int
    //TODO - remove
    override fun gameTicks(): Int = 0
}