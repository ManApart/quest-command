package inventory.equipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item
import core.gameState.body.Slot

class EquipItemEvent(val creature: Creature, val item: Item, val slot: Slot? = null) : Event