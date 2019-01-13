package inventory.dropItem

import core.events.Event
import core.gameState.GameState
import core.gameState.Item
import core.gameState.Target
import java.lang.IllegalArgumentException

class TransferItemEvent(val item: Item, val source: Target? = null, val destination: Target? = null, val silent: Boolean = false) : Event {
    init {
        if (source == null && destination == null) {
            throw IllegalArgumentException("Source and Destination cannot both be null!")
        }
    }

    override fun gameTicks(): Int {
        return if (source == GameState.player.creature) {
            1
        } else {
            0
        }
    }
}