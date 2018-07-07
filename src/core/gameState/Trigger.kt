package core.gameState

import core.events.Event

class Trigger(val callingEvent: String) {

    fun matches(event: Event): Boolean {
        return event::class.simpleName == callingEvent
    }
}