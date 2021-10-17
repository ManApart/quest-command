package explore.look

import core.events.Event
import core.thing.Thing

class LookEvent(val source: Thing, val thing: Thing? = null) : Event