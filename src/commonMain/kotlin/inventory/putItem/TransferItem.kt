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
import kotlin.math.min

class TransferItem : EventListener<TransferItemEvent>() {

    override suspend fun complete(event: TransferItemEvent) {
        val isTaking = event.creature == event.destination
        val isPlacing = event.creature == event.source
        when {
            isPlacing && !event.destination.isWithinRangeOf(event.creature) -> event.source.display { event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to place in ${event.destination.name}." }
            isTaking && !event.source.isWithinRangeOf(event.creature) -> event.source.display { event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to take from ${event.source.name}." }
            !isOpen(event.source) -> event.source.displayToMe("Can't take ${event.item.name} from ${event.source.name} because it's not an open container.")
            !isOpen(event.destination) -> event.source.displayToMe("Can't place ${event.item.name} in ${event.destination.name} because it's not an open container.")
            else -> moveItemFromSourceToDest(event.source, event.item, event.destination, event.count, event.silent)
        }
    }

    private fun isOpen(container: Thing): Boolean {
        return container.properties.tags.has("Container") && container.properties.tags.has("Open")
    }

    private suspend fun moveItemFromSourceToDest(source: Thing, item: Thing, destination: Thing, desiredCount: Int, silent: Boolean) {
        val count = min(item.properties.getCount(), desiredCount)
        val newStack = if (count == item.properties.getCount()) item else item.copy(count)

        if (count < desiredCount) {
            source.displayToMe("Could only move $count of the desired $desiredCount ${item.name}.")
        }
        if (destination.inventory.attemptToAdd(newStack)) {
            removeFromSource(source, item, count)
            EventManager.postEvent(ItemPickedUpEvent(destination, newStack, silent))
        } else {
            source.displayToMe("Could not find a place for ${item.name}.")
            val canHold = destination.body.getRootPart().properties.values.getString("CanHold").split(",")
            if (canHold.isNotEmpty()) source.displayToMe("${destination.name} can only hold items that are ${canHold.joinToStringOr()}.")
        }
    }

    private suspend fun removeFromSource(source: Thing, item: Thing, count: Int) {
        if (item.properties.getCount() > count) {
            item.properties.incCount(-count)
        } else {
            source.inventory.remove(item)
        }
    }

}