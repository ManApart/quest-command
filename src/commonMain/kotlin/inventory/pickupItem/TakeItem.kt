package inventory.pickupItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.utility.asSubject
import core.utility.isAre
import kotlin.math.min

class TakeItem : EventListener<TakeItemEvent>() {
    override suspend fun complete(event: TakeItemEvent) {
        if (event.creature.canReach(event.item.position)) {
            takeItem(event.creature, event.item, event.count, event.silent)
        } else {
            event.creature.display{event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to take ${event.item}."}
        }
    }

    private suspend fun takeItem(taker: Thing, item: Thing, desiredCount: Int, silent: Boolean) {
        val count = min(item.properties.getCount(), desiredCount)
        val newStack = if (count == item.properties.getCount()) item else item.copy(count)

        if (taker.inventory.attemptToAdd(newStack)) {
            if (item.properties.getCount() > count) {
                item.properties.incCount(-count)
            } else {
                item.location.getLocation().removeThing(item)
            }
            EventManager.postEvent(ItemPickedUpEvent(taker, newStack, silent))
        } else {
            taker.displayToMe("Could not find a place for $item.")
        }
    }
}