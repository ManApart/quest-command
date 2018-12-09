package inventory.unEquipItem

import core.events.EventListener
import core.history.display

class ItemUnEquipped : EventListener<ItemUnEquippedEvent>() {
    override fun execute(event: ItemUnEquippedEvent) {
        display("${event.creature.name} un-equipped ${event.item.name} from ${event.slot.description}.")
    }
}