package inventory.equipItem

import core.events.EventListener
import system.EventManager

class EquipItem : EventListener<EquipItemEvent>() {
    override fun execute(event: EquipItemEvent) {
        if (event.slot != null){
            event.creature.body.equip(event.item, event.slot)
        } else {
            event.creature.body.equip(event.item)
        }
        EventManager.postEvent(ItemEquippedEvent(event.creature, event.item, event.item.getEquippedSlot(event.creature.body)))
    }
}