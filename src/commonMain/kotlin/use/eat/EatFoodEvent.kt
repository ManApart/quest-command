package use.eat

import core.events.Event
import core.thing.Thing

data class EatFoodEvent(val creature: Thing, val food: Thing) : Event