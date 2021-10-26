package explore.examine

import core.Player
import core.events.Event
import core.thing.Thing

class ExamineEvent(val source: Player, val thing: Thing? = null) : Event