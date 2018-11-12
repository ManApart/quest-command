package travel.jump

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.location.LocationNode

class FallEvent(val creature: Creature = GameState.player.creature, val destination: LocationNode, val fallHeight: Int = 0, val reason: String? = null) : Event