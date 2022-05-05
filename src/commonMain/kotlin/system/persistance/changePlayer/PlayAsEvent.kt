package system.persistance.changePlayer

import core.Player
import core.events.Event

class PlayAsEvent(val source: Player, val characterName: String) : Event