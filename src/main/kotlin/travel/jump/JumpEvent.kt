package travel.jump

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Location

class JumpEvent(val creature: Creature = GameState.player.creature, val source: Location = GameState.player.creature.location, val destination: Location, val fallDistance: Int? = null) : Event