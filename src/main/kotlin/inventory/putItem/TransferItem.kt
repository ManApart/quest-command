package inventory.putItem

import core.events.EventListener
import core.target.Target
import core.history.display
import core.utility.StringFormatter
import inventory.pickupItem.ItemPickedUpEvent
import core.events.EventManager

class TransferItem : EventListener<TransferItemEvent>() {

    override fun execute(event: TransferItemEvent) {
        val isTaking = event.mover == event.destination
        val isPlacing = event.mover == event.source
        when {
            isPlacing && !event.destination.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away to place in ${event.destination}.")
            isTaking && !event.source.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away to take from ${event.source}.")
            !isOpen(event.source) -> display("Can't take ${event.item.name} from ${event.source.name} because it's not an open container.")
            !isOpen(event.destination) -> display("Can't place ${event.item.name} in ${event.destination.name} because it's not an open container.")
            else -> moveItemFromSourceToDest(event.source, event.item, event.destination, event.silent)
        }
    }

    private fun isOpen(container: Target): Boolean {
        return container.properties.tags.has("Container") && container.properties.tags.has("Open")
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

}