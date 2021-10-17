package system.persistance.newGame

import core.Player
import core.events.Event
import core.thing.Thing

class CreateNewGameEvent(val source: Player, val saveName: String) : Event