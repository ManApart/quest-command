package inventory.dropItem

import core.events.EventListener
import core.gameState.*
import core.gameState.Target
import core.history.display
import interact.scope.ScopeManager
import inventory.equipItem.EquipItemEvent
import inventory.pickupItem.ItemPickedUpEvent
import system.EventManager
import system.location.LocationManager

class TransferItem : EventListener<TransferItemEvent>() {
    override fun execute(event: TransferItemEvent) {
        when {
            event.destination == null -> dropItem(event.source!!, event.item, event.silent)
            event.source != null && !isOpen(event.source) -> display("Can't take ${event.item.name} from ${event.source.name}.")
            !isOpen(event.destination) -> display("Can't place ${event.item.name} in ${event.destination.name}.")
            event.destination is Activator -> placeItemInActivator(event.source, event.item, event.destination, event.silent)
            event.destination is Item -> placeItemInItem(event.source, event.item, event.destination, event.silent)
            event.destination is Creature -> placeItemInCreature(event.source, event.item, event.destination, event.silent)
        }
    }

    private fun dropItem(source: Target, item: Item, silent: Boolean) {
        source.inventory.remove(item)
        item.location = source.location
        ScopeManager.getScope(source.location).addTarget(item)
        EventManager.postEvent(ItemDroppedEvent(source, item, silent))
    }

    private fun isOpen(container: Target): Boolean {
        return container.properties.tags.has("Container") && container.properties.tags.has("Open")
    }

    private fun placeItemInActivator(source: Target?, item: Item, destination: Activator, silent: Boolean) {
        if (!item.canBeHeldByContainerWithProperties(destination.properties)) {
            val acceptedTypes = destination.properties.values.getList("CanHold")
            display("${item.name} cannot be placed in ${destination.name} because it only takes things that are ${acceptedTypes.joinToString(" or ")}.")

        } else if (!destination.creature.inventory.hasCapacityFor(item, destination.properties.values.getInt("Capacity"))) {
            display("${item.name} is too heavy to fit in ${destination.name}.")

        } else {
            placeItem(source, item, destination, destination.creature.inventory, silent)
        }
    }

    private fun placeItemInCreature(source: Target?, item: Item, destination: Creature, silent: Boolean) {
        if (item.canEquipTo(destination.body) && destination.body.getEmptyEquipSlot(item) != null) {
            val slot = destination.body.getEmptyEquipSlot(item)
            removeFromSource(source, item, destination)
            destination.inventory.add(Item(item, 1))
            EventManager.postEvent(EquipItemEvent(destination, item, slot))
        } else {
            val candidates = destination.inventory.findSubInventoryFor(item)
            if (candidates.isEmpty()) {
                display("Could not find a place for $item!")
            } else {
                placeItem(source, item, destination, candidates.first().inventory, silent)
            }
        }
    }

    private fun placeItemInItem(source: Target?, item: Item, destination: Item, silent: Boolean) {
        if (!item.canBeHeldByContainerWithProperties(destination.properties)) {
            val acceptedTypes = destination.properties.values.getList("CanHold")
            display("${item.name} cannot be placed in ${destination.name} because it only takes things that are ${acceptedTypes.joinToString(" or ")}.")

        } else if (!destination.inventory.hasCapacityFor(item, destination.properties.values.getInt("Capacity"))) {
            display("${item.name} is too heavy to fit in ${destination.name}.")

        } else {
            placeItem(source, item, destination, destination.inventory, silent)
        }
    }

    private fun placeItem(source: Target?, item: Item, destination: Target, inventory: Inventory, silent: Boolean) {
        removeFromSource(source, item, destination)
        inventory.add(Item(item, 1))
        EventManager.postEvent(ItemPickedUpEvent(destination, item, silent))
    }

    private fun removeFromSource(source: Target?, item: Item, destination: Target) {
        if (source != null) {
            source.inventory.remove(item)
        } else {
            ScopeManager.getScope(destination.location).removeTarget(item)
            item.properties.values.put("locationDescription", "")
            item.location = LocationManager.NOWHERE_NODE
        }
    }

}