package explore.look

import core.events.Event
import core.target.Target

class LookEvent(val source: Target, val target: Target? = null) : Event