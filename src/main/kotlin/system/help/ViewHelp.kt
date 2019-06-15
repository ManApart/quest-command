package system.help

import core.commands.Command
import core.commands.CommandParser
import core.commands.UnknownCommand
import core.commands.removeFirstItem
import core.events.EventListener
import core.gameState.Target

import core.history.display
import core.utility.StringFormatter
import interact.eat.EatFoodEvent
import interact.scope.ScopeManager
import status.statChanged.StatChangeEvent
import system.EventManager

class ViewHelp : EventListener<ViewHelpEvent>() {
    val description = HelpCommand().getDescription()

    override fun execute(event: ViewHelpEvent) {
        when {
            event.commandManual != null -> printManual(event.commandManual)
            event.commandGroups && event.args.isEmpty() -> printCommandGroupsSummary()
            event.commandGroups && event.args.isNotEmpty() && event.args[0] == "all" -> printCommandGroupsDetail()
            event.commandGroups && event.args.isNotEmpty() -> printCommandGroup(event.args)
            else -> display(description)
        }
    }

    private fun printManual(command: Command) {
        display(getTitle(command) + command.getManual())
    }

    private fun printCommandGroupsSummary() {
        val groups = HashMap<String, MutableList<String>>()
        CommandParser.commands.forEach { command ->
            run {
                if (!groups.containsKey(command.getCategory()[0])) {
                    groups[command.getCategory()[0]] = ArrayList()
                }
                groups[command.getCategory()[0]]?.add(command.name)
            }
        }
        var groupList = ""
        groups.toSortedMap().forEach {
            it.value.sort()
            groupList += "${it.key}:\n\t${it.value.joinToString(", ")}\n"
        }
        display("Help <Group Name> to learn about one of the following groups:\n$groupList")
    }

    private fun printCommandGroupsDetail() {
        val groups = HashMap<String, MutableList<String>>()
        CommandParser.commands.forEach { command ->
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
        display("Help <Group Name> to learn about one of the following groups:\n$groupList")
    }

    private fun printCommandGroup(args: List<String>) {
        var description = "Help <Command> to learn more about on of the following topics:\n"
        //TODO - handle sub-categories
        //TODO - sort alphabetically
        CommandParser.commands.forEach { command ->
            if (command.getCategory().map { it.toLowerCase() }.toTypedArray() contentEquals args.toTypedArray()) {
                description += command.getDescription() + "\n"
            }
        }
        display(description)
    }

    private fun getTitle(command: Command): String {
        val title = command.getAliases()[0]
        val aliases = removeFirstItem(command.getAliases()).joinToString(", ")
        return "$title ($aliases):"
    }

}