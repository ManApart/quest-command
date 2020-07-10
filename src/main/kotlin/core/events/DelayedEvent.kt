package core.events

import core.target.Target

interface DelayedEvent {
    val source: Target
    var timeLeft: Int
    fun getActionEvent(): Event
}