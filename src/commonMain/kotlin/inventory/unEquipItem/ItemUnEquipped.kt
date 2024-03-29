package inventory.unEquipItem

import core.events.EventListener
import core.history.display

class ItemUnEquipped : EventListener<ItemUnEquippedEvent>() {
    override suspend fun complete(event: ItemUnEquippedEvent) {
        event.creature.display("${event.creature.name} un-equipped ${event.item.name} from ${event.slot.description}.")
    }
}