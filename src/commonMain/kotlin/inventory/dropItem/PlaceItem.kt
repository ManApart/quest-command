package inventory.dropItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.thing.Thing
import core.utility.asSubject
import core.utility.isAre
import explore.listen.addSoundEffect
import traveling.position.Vector
import kotlin.math.min

class PlaceItem : EventListener<PlaceItemEvent>() {
    override suspend fun complete(event: PlaceItemEvent) {
        if (event.creature.canReach(event.position)) {
            placeItem(event.creature, event.item, event.position, event.count, event.silent)
        } else {
            event.creature.display { event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to place at ${event.position}." }
        }
    }

    private suspend fun placeItem(source: Thing, item: Thing, position: Vector, desiredCount: Int, silent: Boolean) {
        val count = min(item.properties.getCount(), desiredCount)
        val newStack = if (count == item.properties.getCount()) item else item.copy(count)
        if (item.properties.getCount() > count) {
            item.properties.incCount(-count)
        } else {
            source.inventory.remove(item)
        }
        newStack.location = source.location
        newStack.position = position
        source.location.getLocation().addThing(newStack)
        EventManager.postEvent(ItemDroppedEvent(source, newStack, silent))
        source.addSoundEffect("Placed Item", "a soft thud", 1)
    }
}