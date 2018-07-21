package inventory.unEquipItem

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class UnEquipItemEvent(val creature: Creature, val item: Item) : Event