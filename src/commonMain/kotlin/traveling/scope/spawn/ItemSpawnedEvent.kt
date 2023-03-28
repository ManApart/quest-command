package traveling.scope.spawn

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

data class ItemSpawnedEvent(val item: Thing, val thing: Thing?, val thingLocation: LocationNode) : Event