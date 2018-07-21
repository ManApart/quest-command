package inventory.equipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class EquipItemEvent(val source: Creature, val item: Item, val bodyPart: String? = null) : Event