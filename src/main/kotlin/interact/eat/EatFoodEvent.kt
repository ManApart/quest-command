package interact.eat

import core.events.Event
import core.gameState.Target

class EatFoodEvent(val creature: Target, val food: Target) : Event