package system.persistance.createPlayer

import core.Player
import core.events.Event

class CreateCharacterEvent(val source: Player, val characterName: String) : Event