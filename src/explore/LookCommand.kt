package explore

import core.commands.Command
import core.gameState.GameState
import interact.ScopeManager
import core.gameState.targetsToString

class LookCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Look", "ls", "Examine", "Exa")
    }

    override fun getDescription(): String {
        return "Look:\n\tExamine your surroundings"
    }

    override fun getManual(): String {
        return "\n\tLook - View the objects you can interact with." +
                "\n\tLook <target> - Look at a specific target."
    }

    override fun execute(args: List<String>) {
        if (args.isEmpty()) {
            if (ScopeManager.getTargets().size > 1){
                val targetList = targetsToString(ScopeManager.getTargets().filterNot { it == GameState.player })
                println("You find yourself surrounded by $targetList")
            } else {
                println("You don't see anything of use")
            }
        } else if (ScopeManager.targetExists(args)) {
            val target = ScopeManager.getTarget(args)
            println(target.description)
        } else {
            println("Couldn't find ${args.joinToString(" ")}")
        }
    }
}