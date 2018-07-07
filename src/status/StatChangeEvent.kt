package status

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Stat

class StatChangeEvent(val target: Creature = GameState.player, val source: String, val type: Stat.StatType, val amount: Int) : Event