package inventory.equipItem

import core.events.EventListener
import core.history.display
import core.utility.StringFormatter
import core.events.EventManager

class EquipItem : EventListener<EquipItemEvent>() {
    override fun execute(event: EquipItemEvent) {
        if (!event.item.isWithinRangeOf(event.creature)) {
            display(StringFormatter.getSubject(event.creature) + " " + StringFormatter.getIsAre(event.creature) + " too far away to equip ${event.item}.")
        } else {
            if (event.slot != null) {
                event.creature.body.equip(event.item, event.slot)
            } else {
                event.creature.body.equip(event.item)
            }
            EventManager.postEvent(ItemEquippedEvent(event.creature, event.item, event.item.getEquippedSlot(event.creature.body)))
        }
    }
}