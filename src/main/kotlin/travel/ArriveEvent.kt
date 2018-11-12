package travel

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.location.LocationNode

class ArriveEvent(val creature: Creature = GameState.player.creature, val origin: LocationNode = GameState.player.creature.location, val destination: LocationNode, val method: String) : Event
