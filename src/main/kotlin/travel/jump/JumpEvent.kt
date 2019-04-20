package travel.jump

import core.events.Event
import core.gameState.Target
import core.gameState.GameState
import core.gameState.location.LocationNode

class JumpEvent(val creature: Target = GameState.player, val source: LocationNode = GameState.player.location, val destination: LocationNode, val fallDistance: Int? = null) : Event