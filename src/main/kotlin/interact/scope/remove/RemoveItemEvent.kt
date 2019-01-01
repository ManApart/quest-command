package interact.scope.remove

import core.events.Event
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item

class RemoveItemEvent(val source: Creature = GameState.player.creature, val item: Item) : Event