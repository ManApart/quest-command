package interact

import core.commands.Args
import core.commands.Command
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

    override fun execute(keyword: String, arguments: List<String>) {
        val args = Args(arguments, delimiters)

        if (args.isEmpty()) {
            println("What do you want to use?")
        } else {
            if (targetExists(args.argGroups[0])) {
                val source = findTarget(args.argGroups[0])
                if (args.argGroups.size > 1) {
                    if (targetExists(args.argGroups[1])) {
                        val target = findTarget(args.argGroups[1])
                        EventManager.postEvent(UseEvent(source, target))
                    } else {
                        printDescription(source)
                    }
                } else {
                    printDescription(source)
                }
            } else {
                println("Couldn't find $args")
            }
        }
    }

    private fun targetExists(args: List<String>): Boolean {
        return ScopeManager.targetExists(args) || GameState.player.creature.inventory.itemExists(args)
    }

    private fun findTarget(args: List<String>): Target {
        return if (GameState.player.creature.inventory.itemExists(args)) {
            GameState.player.creature.inventory.getItem(args)
        } else {
            ScopeManager.getTarget(args)
        }
    }

    private fun printDescription(firstTarget: Target) {
        val description = if (firstTarget.description.isNotBlank()) firstTarget.description else "Not much to say."
        println("${firstTarget.name}: $description")
    }

}