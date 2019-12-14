package status

import core.events.Event
import core.target.Target

class CreatureDiedEvent(val creature: Target) : Event