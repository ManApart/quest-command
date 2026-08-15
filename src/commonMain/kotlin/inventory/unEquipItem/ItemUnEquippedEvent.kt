package inventory.unEquipItem

import core.body.EquipTarget
import core.events.Event
import core.thing.Thing

class ItemUnEquippedEvent(val creature: Thing, val item: Thing, val target: EquipTarget) : Event
