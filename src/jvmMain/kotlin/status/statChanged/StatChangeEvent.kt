package status.statChanged

import core.events.Event
import core.thing.Thing

class StatChangeEvent(val thing: Thing, val sourceOfChange: String, val statName: String, val amount: Int, val silent: Boolean = false) : Event {
    override fun toString(): String {
        return "${thing.name} $statName changed $amount by $sourceOfChange."
    }
}