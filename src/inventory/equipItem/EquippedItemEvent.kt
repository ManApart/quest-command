package inventory.equipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class EquippedItemEvent(val source: Creature, val item: Item) : Event