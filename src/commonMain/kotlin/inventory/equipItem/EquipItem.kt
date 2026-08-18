package inventory.equipItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.asSubject
import core.utility.isAre
import explore.listen.addSoundEffect

class EquipItem : EventListener<EquipItemEvent>() {
    override suspend fun complete(event: EquipItemEvent) {
        if (!event.item.isWithinRangeOf(event.creature)) {
            event.creature.display{event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to equip ${event.item}."}
        } else {
            val body = event.creature.body
            val target = if (event.target != null) {
                body.equip(event.item, event.target)
            } else {
                body.equip(event.item)
            }
            EventManager.postEvent(ItemEquippedEvent(event.creature, event.item, target))
            event.creature.addSoundEffect("Equipping", "the tightening of straps and muscle")
        }
    }
}
