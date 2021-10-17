package system.persistance.changePlayer

import core.Player
import core.events.Event
import core.thing.Thing

class PlayAsEvent(val source: Player, val saveName: String) : Event