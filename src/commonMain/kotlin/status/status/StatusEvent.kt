package status.status

import core.Player
import core.events.Event
import core.events.TemporalEvent
import core.thing.Thing

class StatusEvent(val source: Player, val examined: Thing) : Event