package traveling.jump

import core.GameState
import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

class JumpEvent(val creature: Thing, val source: LocationNode = GameState.player.thing.location, val destination: LocationNode, val fallDistance: Int? = null) : Event
