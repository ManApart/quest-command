package inventory.dropItem

import core.events.EventListener
import core.gameState.*
import core.gameState.Target
import core.history.display
import interact.scope.ScopeManager
import inventory.equipItem.EquipItemEvent
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

    private fun placeItemInContainer(destination: Target, event: PlaceItemEvent) {
        when {
            !isOpenContainer(destination) -> display("Can't place ${event.item.name} in ${destination.name}.")
            destination is Activator -> placeItemInActivator(event, destination)
            destination is Item -> placeItemInItem(event, destination)
            destination is Creature -> placeItemInCreature(event, destination)
        }

    }

    private fun isOpenContainer(destination: Target) =
            destination.properties.tags.has("Container") || !destination.properties.tags.has("Open")

    private fun placeItemInActivator(event: PlaceItemEvent, destination: Activator) {
        if (!event.item.canBeHeldByContainerWithProperties(destination.properties)) {
            val acceptedTypes = destination.properties.values.getList("CanHold")
            display("${event.item.name} cannot be placed in ${destination.name} because it only takes things that are ${acceptedTypes.joinToString(" or ")}.")

        } else if (!destination.creature.inventory.hasCapacityFor(event.item, destination.properties.values.getInt("Capacity"))) {
            display("${event.item.name} is too heavy to fit in ${destination.name}.")

        } else {
            placeItem(event, destination.creature.inventory)
        }
    }

    private fun placeItemInCreature(event: PlaceItemEvent, destination: Creature) {
        if (event.item.canEquipTo(destination.body) && destination.body.getEmptyEquipSlot(event.item) != null) {
            val slot = destination.body.getEmptyEquipSlot(event.item)
            event.source.inventory.remove(event.item)
            destination.inventory.add(Item(event.item, 1))
            EventManager.postEvent(EquipItemEvent(destination, event.item, slot))
        } else {
            val candidates = destination.inventory.findSubInventoryFor(event.item)
            if (candidates.isEmpty()) {
                display("Could not find a place for ${event.item}!")
            } else {
                placeItem(event, candidates.first().inventory)
            }
        }
    }

    private fun placeItemInItem(event: PlaceItemEvent, destination: Item) {
        if (!event.item.canBeHeldByContainerWithProperties(destination.properties)) {
            val acceptedTypes = destination.properties.values.getList("CanHold")
            display("${event.item.name} cannot be placed in ${destination.name} because it only takes things that are ${acceptedTypes.joinToString(" or ")}.")

        } else if (!destination.inventory.hasCapacityFor(event.item, destination.properties.values.getInt("Capacity"))) {
            display("${event.item.name} is too heavy to fit in ${destination.name}.")

        } else {
            placeItem(event, destination.inventory)
        }
    }

    private fun placeItem(event: PlaceItemEvent, destination: Inventory) {
        event.source.inventory.remove(event.item)
        destination.add(Item(event.item, 1))
        EventManager.postEvent(ItemPickedUpEvent(event.destination!!, event.item, event.silent))
    }

}