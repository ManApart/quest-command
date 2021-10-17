package system.alias

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing

class AliasCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Alias", "al")
    }

    override fun getDescription(): String {
        return "Manage shortcuts for commands."
    }

    override fun getManual(): String {
        return """ 
    Alias - list existing aliases.
    Alias create <word> <meaning> - from now on typing <word> will be the same as typing <meaning>.
    Alias delete <word> - remove that alias.
    Hint: When creating a command you can use a single & to pipe multiple commands together. The single & will be replaced by && when the command is used.
        """

    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            EventManager.postEvent(ListAliasesEvent(source))
        } else {
            when (args.first()) {
                "create" -> createAlias(source, args)
                "delete" -> deleteAlias(source, args)
                "clear" -> deleteAlias(source, args)
                "list" -> EventManager.postEvent(ListAliasesEvent(source))
                "ls" -> EventManager.postEvent(ListAliasesEvent(source))
                else -> source.displayToMe("Did not understand " + args.joinToString(" ") +". Did you forget a create or delete?")
            }
        }
    }


    private fun createAlias(source: Thing, args: List<String>) {
        if (args.size <= 2) {
            source.displayToMe("Must give an alias followed by a command.")
        } else {
            val alias = args[1]
            val command = args.subList(2, args.size).joinToString(" ")
            EventManager.postEvent(CreateAliasEvent(source, alias, command))
        }
    }

    private fun deleteAlias(source: Thing, args: List<String>) {
        if (args.size != 2) {
            //TODO - get from command parser
            val aliases = listOf("alias1", "alias2")
            val message = "Delete which alias?\n\t${aliases.joinToString(", ")}"
            val response = ResponseRequest(message, aliases.associateWith { "alias delete $it" })
            CommandParsers.setResponseRequest(response)
        } else {
            EventManager.postEvent(DeleteAliasEvent(source, args[1]))
        }
    }
}