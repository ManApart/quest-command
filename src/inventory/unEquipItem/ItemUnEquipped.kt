package inventory.unEquipItem

import core.events.EventListener

class ItemUnEquipped : EventListener<ItemUnEquippedEvent>() {
    override fun execute(event: ItemUnEquippedEvent) {
        println("${event.creature.name} un-equipped ${event.item.name} from ${event.slot.description}.")
    }
}