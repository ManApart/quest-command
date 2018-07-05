package gameState

import events.Event

class Trigger(val callingEvent: String) {

    fun matches(event: Event): Boolean {
        return event::class.simpleName == callingEvent
    }
}