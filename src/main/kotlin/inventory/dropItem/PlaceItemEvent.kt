package inventory.dropItem

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item

class PlaceItemEvent(val source: Creature, val item: Item, val silent: Boolean = false) : Event {
    override fun gameTicks(): Int {
        return if (source == GameState.player.creature) {
            1
        } else {
            0
        }
    }
}