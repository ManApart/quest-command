package inventory.equipItem

import core.events.EventListener

class ItemEquipped : EventListener<ItemEquippedEvent>() {
    override fun execute(event: ItemEquippedEvent) {
        println("${event.creature.name} equipped ${event.item.name} to ${event.slot.description}.")
    }
}