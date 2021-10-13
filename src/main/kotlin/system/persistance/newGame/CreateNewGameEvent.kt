package system.persistance.newGame

import core.Player
import core.events.Event
import core.target.Target

class CreateNewGameEvent(val source: Player, val saveName: String) : Event