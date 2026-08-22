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
            event.creature.display { event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to take ${event.item}." }
        }
    }

    private suspend fun takeItem(taker: Thing, item: Thing, silent: Boolean) {
        if (!taker.hasRoomFor(item)){
            taker.displayToMe("You don't have room to take ${item.name}.")
            return
        }
        if (item.properties.getCount() > 1) {
            val leftOvers = item.copy(count = item.properties.getCount() - 1)
            item.properties.setCount(1)
            item.location.getLocation().addThing(leftOvers)
        }
        take(taker, item, silent)
    }

    private suspend fun take(taker: Thing, item: Thing, silent: Boolean) {
        val previous = item.location
        if (taker.attemptToAdd(item)) {
            previous.getLocation().removeThing(item)
            EventManager.postEvent(ItemPickedUpEvent(taker, item, silent))
        } else {
            taker.displayToMe("Could not find a place for ${item.name}.")
        }
    }
}
