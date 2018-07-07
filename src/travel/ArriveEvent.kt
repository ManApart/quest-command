package travel

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Location

class ArriveEvent(val creature: Creature = GameState.player, val origin: Location = GameState.player.location, val destination: Location) : Event
