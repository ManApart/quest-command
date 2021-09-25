package core.properties.propValChanged

import core.GameState
import core.events.Event
import core.target.Target

class PropertyStatChangeEvent(val target: Target, val sourceOfChange: String, val statName: String, val amount: Int, val silent: Boolean = false) : Event {
    override fun toString(): String {
        return "${target.name} $statName changed $amount by $sourceOfChange."
    }
}