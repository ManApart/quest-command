package inventory.unEquipItem

import core.events.EventListener
import system.EventManager

class UnEquipItem : EventListener<UnEquipItemEvent>() {
    override fun execute(event: UnEquipItemEvent) {
        val slot = event.item.getEquippedSlot(event.creature.body)
        event.creature.body.unEquip(event.item)
        EventManager.postEvent(UnEquippedItemEvent(event.creature, event.item, slot))
    }
}