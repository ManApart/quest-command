package commands

import gameState.ScopeManager
import processing.ClimbManager

class ClimbCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Climb", "c", "Scale")
    }

    override fun getDescription(): String {
        return "Climb:\n\tClimb over obstacles"
    }

    override fun getManual(): String {
        return "\n\tClimb <target> - Climb the target"
    }

    override fun execute(args: List<String>) {
        val argsString = args.joinToString(" ")
        if (args.isEmpty()) {
            println("${args.joinToString(" ")} not found!")
        } else {
            if (ScopeManager.targetExists(argsString)) {
                ClimbManager.attemptToClimb(ScopeManager.getTarget(argsString))
            } else {
                println("${args.joinToString(" ")} not found!")
            }
        }
    }
}