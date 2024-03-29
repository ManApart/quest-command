package inventory.equipItem

import core.events.EventListener
import core.history.display

class ItemEquipped : EventListener<ItemEquippedEvent>() {
    override suspend fun complete(event: ItemEquippedEvent) {
       event.creature.display("${event.creature.name} equipped ${event.item.name} to ${event.slot.description}.")
    }
}