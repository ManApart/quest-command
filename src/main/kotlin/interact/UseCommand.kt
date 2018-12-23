package interact

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import interact.interaction.InteractEvent
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
        return "\n\tUse <item> - Interact with an item or target" +
                "\n\tUse <item> on <target> - Use an item on a target."
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters)

        if (arguments.isEmpty()) {
            display("What do you want to use?")
        } else {
            if (targetExists(arguments.argGroups[0])) {
                val source = findTarget(arguments.argGroups[0])
                if (arguments.argGroups.size > 1) {
                    if (targetExists(arguments.argGroups[1])) {
                        val target = findTarget(arguments.argGroups[1])
                        EventManager.postEvent(UseEvent(source, target))
                    } else {
                        display("Couldn't find ${arguments.argStrings[1]}")
                    }
                } else {
                    EventManager.postEvent(InteractEvent(source))
                }
            } else {
                display("Couldn't find $arguments")
            }
        }
    }

    private fun targetExists(args: List<String>): Boolean {
        return ScopeManager.targetExists(args) || GameState.player.creature.inventory.exists(args)
    }

    private fun findTarget(args: List<String>): Target {
        return if (GameState.player.creature.inventory.exists(args)) {
            GameState.player.creature.inventory.getItem(args)
        } else {
            ScopeManager.getTarget(args)
        }
    }

    private fun printDescription(firstTarget: Target) {
        val description = if (firstTarget.description.isNotBlank()) firstTarget.description else "Not much to say."
        display("${firstTarget.name}: $description")
    }

}