package core.commands.commandEvent

import core.Player
import core.events.Event

class CommandEvent(val source: Player, val command: String) : Event