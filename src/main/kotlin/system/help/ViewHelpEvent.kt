package system.help

import core.commands.Command
import core.events.Event
import core.thing.Thing

class ViewHelpEvent(val source: Thing, val commandGroups: Boolean = false, val commandManual: Command? = null, val args: List<String> = listOf()) : Event