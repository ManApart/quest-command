package traveling.scope.remove

import core.events.EventListener

class RemoveItem : EventListener<RemoveItemEvent>() {
    override suspend fun complete(event: RemoveItemEvent) {
        event.source.remove(event.item)
    }
}
