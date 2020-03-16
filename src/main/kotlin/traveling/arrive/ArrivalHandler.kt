package traveling.arrive

import core.events.EventListener

class ArrivalHandler : EventListener<ArriveEvent>() {
    override fun execute(event: ArriveEvent) {
        event.origin.location.getLocation().removeTarget(event.creature)
        event.destination.location.getLocation().addTarget(event.creature, listOf("me", "self"))
    }
}