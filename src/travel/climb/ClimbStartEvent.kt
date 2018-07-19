package travel.climb

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target

class ClimbStartEvent(val creature: Creature = GameState.player, val target: Target, val force: Boolean = false) : Event