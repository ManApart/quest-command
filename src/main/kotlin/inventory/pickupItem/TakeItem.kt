package inventory.pickupItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.utility.isAre
import core.utility.asSubject

class TakeItem : EventListener<TakeItemEvent>() {
    override fun execute(event: TakeItemEvent) {
        if (event.taker.canReach(event.item.position)) {
            takeItem(event.taker, event.item, event.silent)
        } else {
            event.taker.display{event.taker.asSubject(it) + " " + event.taker.isAre(it) + " too far away to take ${event.item}."}
        }
    }

    private fun takeItem(taker: Thing, item: Thing, silent: Boolean) {
        val newStack = item.copy(1)
        if (taker.inventory.attemptToAdd(newStack)) {
            if (item.properties.getCount() > 1) {
                item.properties.incCount(-1)
            } else {
                item.location.getLocation().removeThing(item)
            }
            EventManager.postEvent(ItemPickedUpEvent(taker, newStack, silent))
        } else {
            taker.displayToMe("Could not find a place for $item.")
        }
    }
}