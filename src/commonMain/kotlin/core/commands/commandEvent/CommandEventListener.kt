package core.commands.commandEvent

import core.commands.CommandParsers
import core.events.EventListener

class CommandEventListener : EventListener<CommandEvent>() {
    override fun execute(event: CommandEvent) {
        CommandParsers.parseCommand(event.source, event.command)
    }
}