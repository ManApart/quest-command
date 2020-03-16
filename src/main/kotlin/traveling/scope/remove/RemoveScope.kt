package traveling.scope.remove

import core.events.EventListener

class RemoveScope : EventListener<RemoveScopeEvent>() {
    override fun execute(event: RemoveScopeEvent) {
        event.target.location.getLocation().removeTarget(event.target)
    }
}