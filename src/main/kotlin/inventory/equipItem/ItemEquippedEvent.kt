package inventory.equipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item
import core.gameState.body.Slot

class ItemEquippedEvent(val creature: Creature, val item: Item, val slot: Slot) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}