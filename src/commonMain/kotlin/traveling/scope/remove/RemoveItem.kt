package traveling.scope.remove

import core.events.EventListener

class RemoveItem : EventListener<RemoveItemEvent>() {
    override suspend fun execute(event: RemoveItemEvent) {
        event.source.inventory.remove(event.item)
    }
}