package explore.examine

import core.events.Event
import core.target.Target

class ExamineEvent(val source: Target, val target: Target? = null) : Event