package core.events

import core.thing.Thing

interface DelayedEvent {
    val source: Thing
    var timeLeft: Int
    fun getActionEvent(): Event
}