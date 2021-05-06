package core.commands.commandEvent

import core.commands.CommandParser
import core.events.EventListener

class CommandEventListener : EventListener<CommandEvent>() {
    override fun execute(event: CommandEvent) {
        CommandParser.parseCommand(event.command)
    }
}