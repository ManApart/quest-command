package system.alias

import core.Player
import core.commands.Command
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe

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

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
            return listOf("list", "create", "delete")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
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


    private fun createAlias(source: Player, args: List<String>) {
        if (args.size <= 2) {
            source.displayToMe("Must give an alias followed by a command.")
        } else {
            val alias = args[1]
            val command = args.subList(2, args.size).joinToString(" ")
            EventManager.postEvent(CreateAliasEvent(source, alias, command))
        }
    }

    private fun deleteAlias(source: Player, args: List<String>) {
        if (args.size != 2) {
            source.respond("No aliases to delete.") {
                message("Delete which alias?")
                //TODO - get from command parser
                options("alias1", "alias2")
                command { "alias delete $it" }
            }
        } else {
            EventManager.postEvent(DeleteAliasEvent(source, args[1]))
        }
    }
}