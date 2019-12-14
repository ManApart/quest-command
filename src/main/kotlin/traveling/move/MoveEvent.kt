package traveling.move

import core.events.Event
import core.GameState
import core.target.Target
import traveling.direction.Vector

class MoveEvent(val creature: Target = GameState.player, val source: Vector = creature.position, val destination: Vector, val silent: Boolean = false) : Event