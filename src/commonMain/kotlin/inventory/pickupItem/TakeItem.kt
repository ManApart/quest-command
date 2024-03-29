package inventory.pickupItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.utility.asSubject
import core.utility.isAre

class TakeItem : EventListener<TakeItemEvent>() {
    override suspend fun complete(event: TakeItemEvent) {
        if (event.creature.canReach(event.item.position)) {
            takeItem(event.creature, event.item, event.silent)
        } else {
            event.creature.display{event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to take ${event.item}."}
        }
    }

    private suspend fun takeItem(taker: Thing, item: Thing, silent: Boolean) {
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