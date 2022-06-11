package core.properties.propValChanged

import core.events.Event
import core.thing.Thing

class PropertyStatChangeEvent(val thing: Thing, val sourceOfChange: String, val statName: String, val amount: Int, val silent: Boolean = false) : Event {
    override fun toString(): String {
        return "${thing.name} $statName changed $amount by $sourceOfChange."
    }
}