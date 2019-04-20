package inventory.equipItem

import core.events.Event
import core.gameState.Target
import core.gameState.body.Slot

class EquipItemEvent(val creature: Target, val item: Target, val slot: Slot? = null) : Event