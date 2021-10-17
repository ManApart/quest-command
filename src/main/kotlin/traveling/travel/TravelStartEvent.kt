package traveling.travel

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

class TravelStartEvent(val creature: Thing, val currentLocation: LocationNode = creature.location, val destination: LocationNode, val quiet: Boolean = false) : Event