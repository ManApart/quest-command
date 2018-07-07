package travel

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Location

class TravelStartEvent(val creature: Creature = GameState.player, val currentLocation: Location = GameState.player.location, val destination: Location) : Event