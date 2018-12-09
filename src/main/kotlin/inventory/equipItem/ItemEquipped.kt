package inventory.equipItem

import core.events.EventListener
import core.history.display

class ItemEquipped : EventListener<ItemEquippedEvent>() {
    override fun execute(event: ItemEquippedEvent) {
        display("${event.creature.name} equipped ${event.item.name} to ${event.slot.description}.")
    }
}