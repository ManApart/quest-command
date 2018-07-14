package travel.jump

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Location

class JumpEvent(val creature: Creature = GameState.player, val source: Location, val destination: Location) : Event