package inventory.dropItem

import core.events.EventListener
import core.gameState.Creature
import core.gameState.Item
import core.gameState.Tags
import core.history.display
import interact.scope.ScopeManager
import inventory.pickupItem.ItemPickedUpEvent
import system.EventManager

class PlaceItem : EventListener<PlaceItemEvent>() {
    override fun execute(event: PlaceItemEvent) {
        if (event.destination == null) {
            dropItem(event)
        } else {
            placeItemInContainer(event.destination, event)
        }
    }

    private fun dropItem(event: PlaceItemEvent) {
        event.source.inventory.remove(event.item)
        ScopeManager.getScope(event.source.location).addTarget(event.item)
        EventManager.postEvent(ItemDroppedEvent(event.source, event.item, event.silent))
    }

    private fun placeItemInContainer(destination: Creature, event: PlaceItemEvent) {
        if (!destination.properties.tags.has("Container") || !destination.properties.tags.has("Open") || destination.getTotalCapacity() == 0) {
            display("Can't place ${event.item.name} in ${destination.name}.")

        } else if (!containerCanTakeType(event.item, destination)) {
            val acceptedTypes = destination.properties.values.getList("CanHold")
            display("${event.item.name} cannot be placed in ${destination.name} because it only takes things that are ${acceptedTypes.joinToString(" or ")}.")

        } else if (destination.getTotalCapacity() - destination.inventory.getTotalWeight() < event.item.weight) {
            display("${event.item.name} is too heavy to fit in ${destination.name}.")

        } else {
            placeItem(event, destination)
        }
    }

    private fun containerCanTakeType(item: Item, container: Creature): Boolean {
        val acceptedTypes = container.properties.values.getList("CanHold")
        return if (acceptedTypes.isEmpty()) {
            true
        } else {
            item.properties.tags.hasAny(Tags(acceptedTypes))
        }
    }

    private fun placeItem(event: PlaceItemEvent, destination: Creature) {
        event.source.inventory.remove(event.item)
        destination.inventory.add(event.item)
        EventManager.postEvent(ItemPickedUpEvent(destination, event.item, event.silent))
    }

}