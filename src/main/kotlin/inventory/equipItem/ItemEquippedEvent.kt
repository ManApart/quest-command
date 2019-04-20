package inventory.equipItem

import core.events.Event
import core.gameState.Target
import core.gameState.body.Slot

class ItemEquippedEvent(val creature: Target, val item: Target, val slot: Slot) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}