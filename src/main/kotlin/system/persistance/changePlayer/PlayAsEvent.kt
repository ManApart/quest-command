package system.persistance.changePlayer

import core.Player
import core.events.Event
import core.target.Target

class PlayAsEvent(val source: Player, val saveName: String) : Event