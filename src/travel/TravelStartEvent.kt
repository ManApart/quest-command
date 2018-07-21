package travel

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Location

class TravelStartEvent(val creature: Creature = GameState.player.creature, val currentLocation: Location = GameState.player.creature.location, val destination: Location) : Event