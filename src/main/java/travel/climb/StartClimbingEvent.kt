package travel.climb

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target

class StartClimbingEvent(val creature: Creature = GameState.player.creature, val target: Target, val force: Boolean = false) : Event