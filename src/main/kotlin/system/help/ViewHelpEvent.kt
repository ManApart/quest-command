package system.help

import core.Player
import core.commands.Command
import core.events.Event

class ViewHelpEvent(val source: Player, val commandGroups: Boolean = false, val commandManual: Command? = null, val args: List<String> = listOf()) : Event