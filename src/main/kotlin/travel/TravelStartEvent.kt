package travel

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.location.LocationNode

class TravelStartEvent(val creature: Creature = GameState.player.creature, val currentLocation: LocationNode = GameState.player.creature.location, val destination: LocationNode) : Event