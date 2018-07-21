package interact

import core.commands.Command
import core.commands.CommandParser
import core.commands.findDelimiter
import core.gameState.GameState
import core.gameState.Target
import system.EventManager

class UseCommand : Command() {
    private val delimiters = listOf("to", "with", "on")
    override fun getAliases(): Array<String> {
        return arrayOf("Use", "u")
    }

    override fun getDescription(): String {
        return "Use:\n\tUse an item or your surroundings"
    }

    override fun getManual(): String {
        return "\n\tUse <item> - Describe an item or activator" +
                "\n\tUse <item> on <target> - Use an item on a target."
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")

        if (args.isEmpty()) {
            println("What do you want to use?")
        } else {
            val delimiter = findDelimiter(args, delimiters)
            val sourceArgs = if (delimiter > 0) args.subList(0, delimiter) else args
            if (targetExists(sourceArgs)) {
                val source = findTarget(sourceArgs)
                if (args.size > delimiter + 1 && delimiter != -1) {
                    val targetArgs = args.subList(delimiter+1, args.size)
                    if (targetExists(targetArgs)) {
                        val target = findTarget(targetArgs)
                        EventManager.postEvent(UseEvent(source, target))
                    } else {
                        printDescription(source)
                    }
                } else {
                    printDescription(source)
                }
            } else {
                println("Couldn't find $argsString")
            }
        }
    }

    private fun targetExists(args: List<String>): Boolean {
        return ScopeManager.targetExists(args) || GameState.player.inventory.itemExists(args)
    }

    private fun findTarget(args: List<String>): Target {
        return if (GameState.player.inventory.itemExists(args)) {
            GameState.player.inventory.getItem(args)
        } else {
            ScopeManager.getTarget(args)
        }
    }

    private fun printDescription(firstTarget: Target) {
        val description = if (firstTarget.description.isNotBlank()) firstTarget.description else "Not much to say."
        println("${firstTarget.name}: $description")
    }

}