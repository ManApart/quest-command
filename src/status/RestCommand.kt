package status

import core.commands.Command
import system.EventManager

class RestCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Rest", "Sleep", "Camp")
    }

    override fun getDescription(): String {
        return "Rest:\n\tRest and regain your stamina."
    }

    override fun getManual(): String {
        return "\n\tRest - Rest for an hour." +
                "\n\tRest <amount> - Rest for a set amount of time."
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(args: List<String>) {
        when {
            args.isEmpty() -> rest(1)
            args.size == 1 && args[0].toIntOrNull() != null -> rest(args[0].toInt())
            else -> println("Unknown params for rest: ${args.joinToString(" ")}")
        }
    }

    private fun rest(hours: Int) {
        EventManager.postEvent(RestEvent(hours))
    }
}


