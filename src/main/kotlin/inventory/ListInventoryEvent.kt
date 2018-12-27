package inventory

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState

class ListInventoryEvent(val target: Creature = GameState.player.creature) : Event