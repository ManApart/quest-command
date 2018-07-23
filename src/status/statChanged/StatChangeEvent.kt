package status.statChanged

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState

class StatChangeEvent(val target: Creature = GameState.player.creature, val source: String, val type: String, val amount: Int) : Event