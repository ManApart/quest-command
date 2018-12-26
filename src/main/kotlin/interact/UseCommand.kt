package interact

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import interact.interaction.InteractEvent
import interact.scope.ScopeManager
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
            val source = ScopeManager.getScope().getTargetIncludingPlayerInventory(arguments.argStrings[0])
            if (source != null) {
                if (arguments.argGroups.size > 1) {
                    val target = ScopeManager.getScope().getTargetIncludingPlayerInventory(arguments.argStrings[1])
                    if (target != null) {
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

}