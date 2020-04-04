package inventory.dropItem

import core.events.EventListener
import inventory.Inventory
import core.target.Target
import traveling.location.location.NOWHERE_NODE
import core.history.display
import core.utility.StringFormatter
import inventory.equipItem.EquipItemEvent
import inventory.pickupItem.ItemPickedUpEvent
import core.events.EventManager

class TransferItem : EventListener<TransferItemEvent>() {
    override fun execute(event: TransferItemEvent) {
        when {
            event.destination == null -> dropItem(event.source!!, event.item, event.silent)
            event.source != null && !event.source.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away to take from ${event.source}.")
            event.source != null && !isOpen(event.source) -> display("Can't take ${event.item.name} from ${event.source.name}.")
            event.source == null && !event.item.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away from ${event.item} to pick it up.")
            !event.destination.isWithinRangeOf(event.mover) -> display(StringFormatter.getSubject(event.mover) + " " + StringFormatter.getIsAre(event.mover) + " too far away to place in ${event.destination}.")
            !isOpen(event.destination) -> display("Can't place ${event.item.name} in ${event.destination.name}.")

//            event.destination.properties.isActivator() -> placeItemInActivator(event.source, event.item, event.destination, event.silent)
//            event.destination.properties.isItem() -> placeItemInItem(event.source, event.item, event.destination, event.silent)
//            event.destination.properties.isCreature() -> placeItemInCreature(event.source, event.item, event.destination, event.silent)
            else -> placeItem(event.source, event.item, event.destination, event.silent)
        }
    }

    private fun dropItem(source: Target, item: Target, silent: Boolean) {
        source.inventory.remove(item)
        item.location = source.location
        source.location.getLocation().addTarget(item)
        EventManager.postEvent(ItemDroppedEvent(source, item, silent))
    }

    private fun isOpen(container: Target): Boolean {
        return container.properties.tags.has("Container") && container.properties.tags.has("Open")
    }

//    private fun placeItemInActivator(source: Target?, item: Target, destination: Target, silent: Boolean) {
//        if (!item.properties.canBeHeldByContainerWithProperties(destination.properties)) {
//            val acceptedTypes = destination.properties.values.getList("CanHold")
//            display("${item.name} cannot be placed in ${destination.name} because it only takes things that are ${acceptedTypes.joinToString(" or ")}.")
//
//        } else if (!destination.inventory.hasCapacityFor(item, destination.properties.values.getInt("Capacity"))) {
//            display("${item.name} is too heavy to fit in ${destination.name}.")
//        } else if (!destination.isWithinRangeOf(source) && source?.isWithinRangeOf(destination) == false) {
//            display(StringFormatter.getSubject(destination) + " " + StringFormatter.getIsAre(destination) + " too far away.")
//        } else {
//            placeItem(source, item, destination, silent)
//        }
//    }
//
//    private fun placeItemInCreature(source: Target?, item: Target, destination: Target, silent: Boolean) {
//        if (item.canEquipTo(destination.body) && destination.body.getEmptyEquipSlot(item) != null) {
//            val slot = destination.body.getEmptyEquipSlot(item)
//            removeFromSource(source, item, destination)
//            destination.inventory.add(item.copy(1))
//            EventManager.postEvent(EquipItemEvent(destination, item, slot))
//        } else {
//            placeItem(source, item, destination, silent)
//        }
//    }
//
//    private fun placeItemInItem(source: Target?, item: Target, destination: Target, silent: Boolean) {
//        if (!item.properties.canBeHeldByContainerWithProperties(destination.properties)) {
//            val acceptedTypes = destination.properties.values.getList("CanHold")
//            display("${item.name} cannot be placed in ${destination.name} because it only takes things that are ${acceptedTypes.joinToString(" or ")}.")
//
//        } else if (!destination.inventory.hasCapacityFor(item, destination.properties.values.getInt("Capacity"))) {
//            display("${item.name} is too heavy to fit in ${destination.name}.")
//
//        } else {
//            placeItem(source, item, destination,silent)
//        }
//    }

    private fun placeItem(source: Target?, item: Target, destination: Target, silent: Boolean) {
        if (destination.inventory.attemptToAdd(item)) {
            removeFromSource(source, item, destination)
            EventManager.postEvent(ItemPickedUpEvent(destination, item, silent))
        } else {
            display("Could not find a place for $item")
        }
    }

    private fun removeFromSource(source: Target?, item: Target, destination: Target) {
        when {
            item.properties.getCount() > 1 -> {
                item.properties.incCount(-1)
            }
            source != null -> {
                source.inventory.remove(item)
            }
            else -> {
                destination.location.getLocation().removeTarget(item)
            }
        }

    }

}