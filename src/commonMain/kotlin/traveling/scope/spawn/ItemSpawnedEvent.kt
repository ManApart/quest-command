package traveling.scope.spawn

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

class ItemSpawnedEvent(val item: Thing, val thing: Thing?, val thingLocation: LocationNode) : Event