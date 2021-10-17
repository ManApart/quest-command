package explore.examine

import core.events.Event
import core.thing.Thing

class ExamineEvent(val source: Thing, val thing: Thing? = null) : Event