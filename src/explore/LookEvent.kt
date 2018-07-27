package explore

import core.events.Event

class LookEvent : Event {

    override fun usesGameTick(): Boolean {
        return false
    }
}