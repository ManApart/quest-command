package travel.climb

import core.events.Event
import core.gameState.GameState
import core.gameState.Target

class StartClimbingEvent(val creature: Target = GameState.player, val target: Target, val force: Boolean = false) : Event