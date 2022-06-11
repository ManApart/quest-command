package traveling.scope.remove

import core.events.EventListener

class RemoveScope : EventListener<RemoveScopeEvent>() {
    override fun execute(event: RemoveScopeEvent) {
        event.thing.location.getLocation().removeThing(event.thing)
    }
}