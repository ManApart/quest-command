package inventory.dropItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.thing.Thing
import core.utility.asSubject
import core.utility.isAre
import explore.listen.addSoundEffect
import traveling.position.Vector

class PlaceItem : EventListener<PlaceItemEvent>() {
    override suspend fun complete(event: PlaceItemEvent) {
        if (event.creature.canReach(event.position)) {
            placeItem(event.creature, event.item, event.position, event.silent)
        } else {
            event.creature.display { event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to place at ${event.position}." }
        }
    }

    private suspend fun placeItem(source: Thing, item: Thing, position: Vector, silent: Boolean) {
        if (item.properties.getCount() > 1) {
            item.properties.incCount(-1)
        } else {
            source.inventory.remove(item)
        }
        item.location = source.location
        item.position = position
        source.location.getLocation().addThing(item)
        EventManager.postEvent(ItemDroppedEvent(source, item, silent))
        source.addSoundEffect("Placed Item", "a soft thud", 1)
    }
}