package explore

import core.commands.Command
import interact.ScopeManager
import system.EventManager

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

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> EventManager.postEvent(LookEvent())
            ScopeManager.targetExists(args) -> EventManager.postEvent(LookEvent(ScopeManager.getTarget(args)))
            else -> println("Couldn't find ${args.joinToString(" ")}.")
        }
    }

}