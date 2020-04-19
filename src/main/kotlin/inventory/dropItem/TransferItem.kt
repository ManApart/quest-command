package inventory.dropItem

import core.events.EventListener
import core.target.Target
import core.history.display
import core.utility.StringFormatter
import inventory.pickupItem.ItemPickedUpEvent
import core.events.EventManager

class TransferItem : EventListener<TransferItemEvent>() {
    override fun execute(event: TransferItemEvent) {
        when {
            event.destination == null -> dropItem(event.source!!, event.item, event.silent)
            !event.destination.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away to place in ${event.destination}.")
            event.source != null && !event.source.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away to take from ${event.source}.")
            event.source == null && !event.item.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away from ${event.item} to pick it up.")
            event.source != null && !isOpen(event.source) -> display("Can't take ${event.item.name} from ${event.source.name} because it's not an open container.")
            !isOpen(event.destination) -> display("Can't place ${event.item.name} in ${event.destination.name} because it's not an open container.")
            event.source == null -> takeItemFromLocation(event.item, event.destination, event.silent)
            else -> moveItemFromSourceToDest(event.source, event.item, event.destination, event.silent)
        }
    }

    private fun isOpen(container: Target): Boolean {
        return container.properties.tags.has("Container") && container.properties.tags.has("Open")
    }

    private fun dropItem(source: Target, item: Target, silent: Boolean) {
        removeFromSource(source, item)
        item.location = source.location
        source.location.getLocation().addTarget(item)
        EventManager.postEvent(ItemDroppedEvent(source, item, silent))
    }

    private fun takeItemFromLocation(item: Target, destination: Target, silent: Boolean) {
        val newStack = item.copy(1)
        if (destination.inventory.attemptToAdd(newStack)) {
            removeFromDestLocation(item, destination)
            EventManager.postEvent(ItemPickedUpEvent(destination, newStack, silent))
        } else {
            display("Could not find a place for $item.")
        }
    }

    private fun moveItemFromSourceToDest(source: Target, item: Target, destination: Target, silent: Boolean) {
        val newStack = item.copy(1)
        if (destination.inventory.attemptToAdd(newStack)) {
            removeFromSource(source, item)
            EventManager.postEvent(ItemPickedUpEvent(destination, newStack, silent))
        } else {
            display("Could not find a place for $item.")
        }
    }

    private fun removeFromSource(source: Target, item: Target) {
        if (item.properties.getCount() > 1) {
            item.properties.incCount(-1)
        } else {
            source.inventory.remove(item)
        }
    }

    private fun removeFromDestLocation(item: Target, destination: Target) {
        if (item.properties.getCount() > 1) {
            item.properties.incCount(-1)
        } else {
            destination.location.getLocation().removeTarget(item)
        }
    }


}