package system.persistance.changePlayer

import core.events.Event
import core.target.Target

class PlayAsEvent(val source: Target, val saveName: String) : Event