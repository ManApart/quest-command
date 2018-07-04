package commands

import gameState.GameState
import gameState.ScopeManager

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
            println("You find yourself surrounded by ${targets.joinToString(", ")}")
        } else if (args[0] == "items") {
            val targets = ScopeManager.getTargets()
            println("You see ${targets.joinToString(", ")}")
        }
    }
}