package inventory.equipItem

import core.events.EventListener

class ItemEquipped : EventListener<EquippedItemEvent>() {
    override fun execute(event: EquippedItemEvent) {
        println("${event.creature.name} equipped ${event.item.name} to ${event.slot.description}.")
    }
}