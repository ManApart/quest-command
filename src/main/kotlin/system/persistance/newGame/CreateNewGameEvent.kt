package system.persistance.newGame

import core.events.Event
import core.target.Target

class CreateNewGameEvent(val source: Target, val saveName: String) : Event