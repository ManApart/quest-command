package inventory.unEquipItem

import core.events.EventListener

class ItemUnEquipped : EventListener<UnEquippedItemEvent>() {
    override fun execute(event: UnEquippedItemEvent) {
        println("${event.creature.name} un-equipped ${event.item.name} from ${event.slot.description}.")
    }
}