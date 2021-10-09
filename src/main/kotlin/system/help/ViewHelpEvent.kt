package system.help

import core.commands.Command
import core.events.Event
import core.target.Target

class ViewHelpEvent(val source: Target, val commandGroups: Boolean = false, val commandManual: Command? = null, val args: List<String> = listOf()) : Event