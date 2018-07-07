package explore

import core.commands.Command
import core.gameState.GameState
import use.ScopeManager
import core.gameState.targetsToString

class LookCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Look", "ls", "Examine")
    }

    override fun getDescription(): String {
        return "Look:\n\tExamine your surroundings"
    }

    override fun getManual(): String {
        return "\n\tLook - View the objects you can interact with." +
                "\n\tLook items - View your items."
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            val targets = ScopeManager.getTargets().filter { !GameState.player.inventory.items.contains(it) }
            val targetList = targetsToString(targets)
            println("You find yourself surrounded by $targetList")
        } else if (args[0] == "items") {
            val targets = ScopeManager.getTargets()
            println("You see ${targets.joinToString(", ")}")
        }
    }
}