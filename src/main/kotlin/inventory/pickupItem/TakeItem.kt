package inventory.pickupItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.StringFormatter

class TakeItem : EventListener<TakeItemEvent>() {
    override fun execute(event: TakeItemEvent) {
        if (event.taker.canReach(event.item.position)) {
            takeItem(event.taker, event.item, event.silent)
        } else {
            display(StringFormatter.getSubject(event.taker) + " " + StringFormatter.getIsAre(event.taker) + " too far away to take ${event.item}.")
        }
    }

    private fun takeItem(taker: Target, item: Target, silent: Boolean) {
        val newStack = item.copy(1)
        if (taker.inventory.attemptToAdd(newStack)) {
            if (item.properties.getCount() > 1) {
                item.properties.incCount(-1)
            } else {
                item.location.getLocation().removeTarget(item)
            }
            EventManager.postEvent(ItemPickedUpEvent(taker, newStack, silent))
        } else {
            display("Could not find a place for $item.")
        }
    }
}