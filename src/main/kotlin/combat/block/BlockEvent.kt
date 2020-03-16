package combat.block

import core.events.Event
import core.target.Target
import traveling.location.location.LocationRecipe


class BlockEvent(val source: Target, val partThatWillShield: LocationRecipe, val partThatWillBeShielded: LocationRecipe) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}