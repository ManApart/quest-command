package traveling.climb

import core.events.Event
import core.thing.Thing
import traveling.location.location.LocationPoint

class ClimbCompleteEvent(val creature: Thing, val climbThing: Thing, val origin: LocationPoint, val destination: LocationPoint) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}