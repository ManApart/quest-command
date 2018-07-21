package travel

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Location

class ArriveEvent(val creature: Creature = GameState.player.creature, val origin: Location = GameState.player.creature.location, val destination: Location, val method: String) : Event
