package status.status

import core.Player
import core.commands.Command
import core.commands.respond
import core.commands.respondSuspend
import core.events.EventManager
import core.history.displayToMe
import core.utility.filterUniqueByName

class StatusCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Status", "Info", "stats", "stat")
    }

    override fun getDescription(): String {
        return "Get information about your or something else's status"
    }

    override fun getManual(): String {
        return """
	Status - Get your current status
	Status <thing> - Get the status of a thing."""
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        when {
            args.isEmpty() && keyword == "status" -> clarifyStatus(source)
            args.isEmpty() -> EventManager.postEvent(StatusEvent(source, source.thing))
            source.thing.currentLocation().getCreatures(argsString).filterUniqueByName().isNotEmpty()-> EventManager.postEvent(StatusEvent(source, source.thing.currentLocation().getCreatures(argsString).filterUniqueByName().first()))
            else -> source.displayToMe("Couldn't find ${args.joinToString(" ")}.")
        }
    }

    private suspend fun clarifyStatus(source: Player) {
        source.respondSuspend("There is nothing to get a status of.") {
            message("Status of what?")
            optionsNamed(source.thing.currentLocation().getCreatures())
            command { "status $it" }
        }
    }


}