package inventory.pickupItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class PickupItemEvent(val source: Creature, val item: Item, val silent: Boolean = false) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}