package travel.move

import core.events.Event
import core.gameState.Target
import core.gameState.GameState
import core.gameState.Vector
import core.gameState.location.LocationNode

class MoveEvent(val creature: Target = GameState.player, val source: Vector = creature.position, val destination: Vector, val silent: Boolean = false) : Event