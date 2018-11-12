package travel.jump

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.location.LocationNode

class JumpEvent(val creature: Creature = GameState.player.creature, val source: LocationNode = GameState.player.creature.location, val destination: LocationNode, val fallDistance: Int? = null) : Event