package explore.look

import core.Player
import core.events.Event
import core.thing.Thing

class LookEvent(val source: Player, val thing: Thing? = null) : Event