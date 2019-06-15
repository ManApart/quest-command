package explore

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

class LookCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Look", "ls", "Examine", "Exa")
    }

    override fun getDescription(): String {
        return "Look:\n\tExamine your surroundings"
    }

    override fun getManual(): String {
        return "\n\tLook all - View the objects you can interact with." +
                "\n\tLook <target> - Look at a specific target."
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argString = args.joinToString(" ")
        when {
            keyword == "look" && args.isEmpty() -> clarifyTarget()
            args.isEmpty() -> EventManager.postEvent(LookEvent())
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(LookEvent())
            ScopeManager.getScope().getTargetsIncludingPlayerInventory(argString).isNotEmpty() -> EventManager.postEvent(LookEvent(ScopeManager.getScope().getTargetsIncludingPlayerInventory(argString).first()))
            else -> display("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private fun clarifyTarget() {
        val targets  = (listOf("all") + ScopeManager.getScope().getAllTargets().map { it.name })
        display("Look at what?\n\t${targets.joinToString(", ")}")
        val response = ResponseRequest(targets.map { it to "look $it" }.toMap())
        CommandParser.responseRequest  = response
    }

}