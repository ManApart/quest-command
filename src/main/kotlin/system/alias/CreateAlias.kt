package system.alias

import core.GameState
import core.commands.CommandParser
import core.events.EventListener
import core.history.display
import core.history.displayYou
import core.target.Target

class CreateAlias : EventListener<CreateAliasEvent>() {
    override fun execute(event: CreateAliasEvent) {
        val alias = event.alias.lowercase()
        val existingCommand = CommandParser.findCommand(alias)
        if (existingCommand != CommandParser.unknownCommand) {
            event.source.displayYou("Cannot create alias for '$alias' because it exists as a built in command or alias for ${existingCommand.name}.")
        } else {
            updateAlias(event.source, alias, event)
        }
    }

    private fun updateAlias(source: Target, alias: String, event: CreateAliasEvent) {
        val replacing = GameState.aliases.containsKey(alias)
        val oldValue = GameState.aliases[alias] ?: ""

        val command = event.command.replace(" & ", " && ")
        GameState.aliases[alias] = command

        if (replacing) {
            source.displayYou("Replaced '$alias'. Changed '$oldValue' to '$command'.")
        } else {
            source.displayYou("Created '$alias' mapped to '$command'.")
        }
    }

}