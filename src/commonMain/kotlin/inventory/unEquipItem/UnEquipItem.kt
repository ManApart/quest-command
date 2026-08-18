package inventory.unEquipItem

import core.events.EventListener
import core.events.EventManager
import core.history.display

class UnEquipItem : EventListener<UnEquipItemEvent>() {
    override suspend fun complete(event: UnEquipItemEvent) {
        val target = event.creature.body.getEquippedTarget(event.item)
        if (target == null) {
            event.creature.display("${event.item} does not seem to be equipped.")
        } else {
            event.creature.body.unEquip(event.item)
            EventManager.postEvent(ItemUnEquippedEvent(event.creature, event.item, target))
        }
    }
}
