package inventory.dropItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class DropItemEvent(val source: Creature, val item: Item) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}