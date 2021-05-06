package system.alias

import core.GameState
import core.commands.CommandParser
import core.events.EventListener
import core.history.display

class CreateAlias : EventListener<CreateAliasEvent>() {
    override fun execute(event: CreateAliasEvent) {
        val alias = event.alias.lowercase()
        val existingCommand = CommandParser.findCommand(alias)
        if (existingCommand != CommandParser.unknownCommand) {
            display("Cannot create alias for '$alias' because it exists as a built in command or alias for ${existingCommand.name}.")
        } else {
            updateAlias(alias, event)
        }
    }

    private fun updateAlias(alias: String, event: CreateAliasEvent) {
        val replacing = GameState.aliases.containsKey(alias)
        val oldValue = GameState.aliases[alias] ?: ""

        val command = event.command.replace(" & ", " && ")
        GameState.aliases[alias] = command

        if (replacing) {
            display("Replaced '$alias'. Changed '$oldValue' to '$command'.")
        } else {
            display("Created '$alias' mapped to '$command'.")
        }
    }

}