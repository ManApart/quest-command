package system.persistance.newGame

import core.Player
import core.events.Event

class CreateNewGameEvent(val source: Player, val saveName: String) : Event