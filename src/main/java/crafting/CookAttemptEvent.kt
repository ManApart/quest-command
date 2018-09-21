package crafting

import core.events.Event
import core.gameState.Activator
import core.gameState.Creature
import core.gameState.Item

class CookAttemptEvent(val source: Creature, val ingredients: List<Item>, val tool: Activator) : Event