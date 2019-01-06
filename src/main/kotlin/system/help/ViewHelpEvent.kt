package system.help

import core.commands.Command
import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class ViewHelpEvent(val commandGroups: Boolean = false, val commandManual: Command? = null, val args: List<String> = listOf()) : Event