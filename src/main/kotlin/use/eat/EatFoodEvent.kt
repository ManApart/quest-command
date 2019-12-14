package use.eat

import core.events.Event
import core.target.Target

class EatFoodEvent(val creature: Target, val food: Target) : Event