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

    override suspend fun execute(event: TransferItemEvent) {
        val isTaking = event.mover == event.destination
        val isPlacing = event.mover == event.source
        when {
            isPlacing && !event.destination.isWithinRangeOf(event.mover) -> event.source.display{event.mover.asSubject(it) + " " + event.mover.isAre(it) + " too far away to place in ${event.destination.name}."}
            isTaking && !event.source.isWithinRangeOf(event.mover) -> event.source.display{event.mover.asSubject(it) + " " + event.mover.isAre(it) + " too far away to take from ${event.source.name}."}
            !isOpen(event.source) -> event.source.displayToMe("Can't take ${event.item.name} from ${event.source.name} because it's not an open container.")
            !isOpen(event.destination) -> event.source.displayToMe("Can't place ${event.item.name} in ${event.destination.name} because it's not an open container.")
            else -> moveItemFromSourceToDest(event.source, event.item, event.destination, event.silent)
        }
    }

    private fun isOpen(container: Thing): Boolean {
        return container.properties.tags.has("Container") && container.properties.tags.has("Open")
    }

    private suspend fun moveItemFromSourceToDest(source: Thing, item: Thing, destination: Thing, silent: Boolean) {
        val newStack = item.copy(1)
        if (destination.inventory.attemptToAdd(newStack)) {
            removeFromSource(source, item)
            EventManager.postEvent(ItemPickedUpEvent(destination, newStack, silent))
        } else {
            source.displayToMe("Could not find a place for ${item.name}.")
            val canHold = destination.body.getRootPart().properties.values.getString("CanHold").split(",")
            if (canHold.isNotEmpty()) source.displayToMe("${destination.name} can only hold items that are ${canHold.joinToStringOr()}.")
        }
    }

    private suspend fun removeFromSource(source: Thing, item: Thing) {
        if (item.properties.getCount() > 1) {
            item.properties.incCount(-1)
        } else {
            source.inventory.remove(item)
        }
    }

}