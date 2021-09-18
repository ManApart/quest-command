package status.statChanged

import core.GameState
import core.events.Event
import core.target.Target

class StatChangeEvent(val target: Target = GameState.player, val sourceOfChange: String, val statName: String, val amount: Int, val silent: Boolean = false) : Event {
    override fun toString(): String {
        return "${target.name} $statName changed $amount by $sourceOfChange."
    }
}