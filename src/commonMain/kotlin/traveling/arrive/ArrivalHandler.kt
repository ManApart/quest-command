package traveling.arrive

import core.GameState
import core.events.EventListener

class ArrivalHandler : EventListener<ArriveEvent>() {
    override suspend fun execute(event: ArriveEvent) {
        event.origin.location.getLocation().removeThing(event.creature)
        event.destination.location.getLocation().addThing(event.creature, listOf("me", "self"))
        if (event.creature.isPlayer()) {
            GameState.getPlayer(event.creature)?.let {
                event.destination.location.discoverSelfAndNeighbors(it.thing)
            }
        }
    }
}