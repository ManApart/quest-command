package inventory.unEquipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class UnEquipItemEvent(val source: Creature, val item: Item, val bodyPart: String? = null) : Event