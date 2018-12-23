package interact

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class EatFoodEvent(val creature: Creature, val food: Item) : Event