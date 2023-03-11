package core.events

import core.thing.Thing

interface TemporalEvent : Event {
    val creature: Thing
    var timeLeft: Int
}