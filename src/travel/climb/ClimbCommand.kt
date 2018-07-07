package travel.climb

import core.commands.Command
import core.commands.removeFirstItem
import core.gameState.GameState
import interact.ScopeManager
import system.EventManager

class ClimbCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Climb", "c", "Scale")
    }

    override fun getDescription(): String {
        return "Climb:\n\tClimb over obstacles"
    }

    override fun getManual(): String {
        return "\n\tClimb <target> - Climb the target" +
                "\n\tClimb down <target> - Climb down the target"
    }

    override fun execute(args: List<String>) {
        var argsString = args.joinToString(" ")
        if (args.isEmpty()) {
            println("${args.joinToString(" ")} not found!")
        } else {
            var upwards = true
            if (args[0] == "down") {
                argsString = removeFirstItem(args).joinToString(" ")
                upwards = false
            } else if (args[0] == "up") {
                argsString = removeFirstItem(args).joinToString(" ")
            }
            if (ScopeManager.targetExists(argsString)) {
                EventManager.postEvent(ClimbAttemptEvent(GameState.player, ScopeManager.getTarget(argsString), upwards))
            } else {
                println("${args.joinToString(" ")} not found!")
            }
        }
    }
}