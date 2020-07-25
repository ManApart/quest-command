package inventory.dropItem

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.target.Target
import core.utility.StringFormatter
import traveling.position.Vector

class PlaceItem : EventListener<PlaceItemEvent>() {
    override fun execute(event: PlaceItemEvent) {
        if (event.source.canReach(event.position)) {
            placeItem(event.source, event.item, event.position, event.silent)
        } else {
            display(StringFormatter.getSubject(event.source) + " " + StringFormatter.getIsAre(event.source) + " too far away to place at ${event.position}.")
        }
    }

    private fun placeItem(source: Target, item: Target, position: Vector, silent: Boolean) {
        if (item.properties.getCount() > 1) {
            item.properties.incCount(-1)
        } else {
            source.inventory.remove(item)
        }
        item.location = source.location
        item.position = position
        source.location.getLocation().addTarget(item)
        EventManager.postEvent(ItemDroppedEvent(source, item, silent))
    }
}