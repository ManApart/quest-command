package inventory.putItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.history.displayToMe
import core.thing.Thing
import core.utility.asSubject
import core.utility.isAre
import core.utility.joinToStringOr
import inventory.pickupItem.ItemPickedUpEvent

class TransferItem : EventListener<TransferItemEvent>() {

    override suspend fun complete(event: TransferItemEvent) {
        val isTaking = event.creature == event.destination
        val isPlacing = event.creature == event.source
        when {
            isPlacing && !event.destination.isWithinRangeOf(event.creature) -> event.source.display{event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to place in ${event.destination.name}."}
            isTaking && !event.source.isWithinRangeOf(event.creature) -> event.source.display{event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to take from ${event.source.name}."}
            !isOpen(event.source) -> event.source.displayToMe("Can't take ${event.item.name} from ${event.source.name} because it's not an open container.")
            !isOpen(event.destination) -> event.source.displayToMe("Can't place ${event.item.name} in ${event.destination.name} because it's not an open container.")
            else -> moveItemFromSourceToDest(event.source, event.item, event.destination, event.silent)
        }
    }

    private fun isOpen(container: Thing) =  container.properties.isOpenContainer()

    private fun moveItemFromSourceToDest(source: Thing, item: Thing, destination: Thing, silent: Boolean) {
        val place = destination.getPlaceFor(item)
        if (place == null) {
            source.displayToMe("Could not find a place for ${item.name}. Use the fit command to see why.")
            return
        }

        val initialCount = item.properties.getCount()
        val leftOvers = if (initialCount > 1) {
            item.properties.setCount(1)
            item.copy(count = initialCount - 1)
        } else null

        if (place.attemptToAdd(item)) {
            source.remove(item)
            leftOvers?.let { source.add(it) }
            EventManager.postEvent(ItemPickedUpEvent(place, item, silent))
        } else {
            source.displayToMe("Could not find a place for ${item.name}.")
            val canHold = destination.properties.values.getString("CanHold").split(",")
            if (canHold.isNotEmpty()) source.displayToMe("${destination.name} can only hold items that are ${canHold.joinToStringOr()}.")
        }
    }
}
