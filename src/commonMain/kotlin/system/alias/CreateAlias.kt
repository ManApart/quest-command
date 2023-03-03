package system.alias

import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe
import core.thing.Thing

class CreateAlias : EventListener<CreateAliasEvent>() {
    override suspend fun complete(event: CreateAliasEvent) {
        val alias = event.alias.lowercase()
        val existingCommand = CommandParsers.findCommand(alias)
        if (existingCommand != CommandParsers.unknownCommand) {
            event.source.displayToMe("Cannot create alias for '$alias' because it exists as a built in command or alias for ${existingCommand.name}.")
        } else {
            updateAlias(event.source.thing, alias, event)
        }
    }

    private fun updateAlias(source: Thing, alias: String, event: CreateAliasEvent) {
        val replacing = GameState.aliases.containsKey(alias)
        val oldValue = GameState.aliases[alias] ?: ""

        val command = event.command.replace(" & ", " && ")
        GameState.aliases[alias] = command

        if (replacing) {
            source.displayToMe("Replaced '$alias'. Changed '$oldValue' to '$command'.")
        } else {
            source.displayToMe("Created '$alias' mapped to '$command'.")
        }
    }

}