package travel.climb

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target

class ClimbAttemptEvent(val source: Creature = GameState.player, val target: Target, val upwards: Boolean = true) : Event