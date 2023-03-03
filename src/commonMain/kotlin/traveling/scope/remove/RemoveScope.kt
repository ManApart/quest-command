package traveling.scope.remove

import core.events.EventListener

class RemoveScope : EventListener<RemoveScopeEvent>() {
    override suspend fun complete(event: RemoveScopeEvent) {
        event.thing.location.getLocation().removeThing(event.thing)
    }
}