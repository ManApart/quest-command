package system.help

import core.Player
import core.commands.Command
import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe
import core.utility.removeFirstItem
import core.utility.toSortedMap

//TODO - can this and ViewWordHelp share code?
class ViewHelp : EventListener<ViewHelpEvent>() {
    val description = HelpCommand().getDescription()

    override suspend fun complete(event: ViewHelpEvent) {
        when {
            event.commandManual != null -> printManual(event.source, event.commandManual)
            event.commandGroups && event.args.isEmpty() -> printCommandGroupsSummary(event.source)
            event.commandGroups && event.args.isNotEmpty() && event.args[0] == "all" -> printCommandGroupsDetail(event.source)
            event.commandGroups && event.args.isNotEmpty() -> printCommandGroup(event.source, event.args)
            else -> event.source.displayToMe(description)
        }
    }

    private fun printManual(source: Player, command: Command) {
        source.displayToMe(getTitle(command) + command.getManual())
    }

    private fun printCommandGroupsSummary(source: Player) {
        val groups = CommandParsers.getGroupedCommands()

        var groupList = ""
        groups.filter { it.key.isNotBlank() }.forEach { entry ->
            val commandNames = entry.value.joinToString(", ") { it.name }
            groupList += "${entry.key}:\n\t${commandNames}\n"
        }
        source.displayToMe("Help <Group Name> to learn about one of the following groups:\n$groupList")
    }

    private fun printCommandGroupsDetail(source: Player) {
        val groups = HashMap<String, MutableList<String>>()
        CommandParsers.commands.forEach { command ->
            run {
                if (!groups.containsKey(command.getCategory()[0])) {
                    groups[command.getCategory()[0]] = ArrayList()
                }
                val details = command.name + ":\n\t\t" + command.getAliases().joinToString(", ")
                groups[command.getCategory()[0]]?.add(details)
            }
        }
        var groupList = ""
        groups.toSortedMap().forEach {
            it.value.sort()
            groupList += "${it.key}:\n\t${it.value.joinToString("\n\t")}\n"
        }
        source.displayToMe("Help <Group Name> to learn about one of the following groups:\n$groupList")
    }

    private fun printCommandGroup(source: Player, args: List<String>) {
        var description = "Help <Command> to learn more about on of the following topics:\n"
        //TODO - handle sub-categories
        //TODO - sort alphabetically
        CommandParsers.commands.forEach { command ->
            if (command.getCategory().map { it.lowercase() }.toTypedArray() contentEquals args.toTypedArray()) {
                description += command.name + ":\n\t" + command.getDescription() + "\n"
            }
        }
        source.displayToMe(description)
    }

    private fun getTitle(command: Command): String {
        val title = command.getAliases()[0]
        val aliases = command.getAliases().removeFirstItem().joinToString(", ")
        return "$title ($aliases):"
    }

}
