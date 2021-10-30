package explore.look

import core.Player
import core.events.Event
import core.thing.Thing
import traveling.location.location.Location

class LookEvent(val source: Player, val thing: Thing? = null, val location: Location? = null) : Event