package explore.look

import core.Player
import core.commands.Command
import core.commands.parseThings
import core.commands.respond
import core.commands.respondSuspend
import core.events.EventManager
import core.history.displayToMe
import traveling.position.ThingAim

class LookCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Look", "ls")
    }

    override fun getDescription(): String {
        return "Observe your surroundings."
    }

    override fun getManual(): String {
        return """
	Look all - View the objects you can interact with.
	Look <thingAim> - Look at a specific thing.
    Look body of <thing> - Look at the body of a thing.
    Look hand of player - Look at the player's right hand."""
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> listOf("all", "body", "hand") + source.getPerceivedThingNames()
            args.last() == "body" || args.last() == "hand" -> listOf("of")
            args.last() == "of" -> source.thing.currentLocation().getThings(perceivedBy = source.thing).map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        when {
            keyword == "look" && args.isEmpty() -> clarifyThing(source)
            args.isEmpty() -> EventManager.postEvent(LookEvent(source))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(LookEvent(source))
            else -> tryAndGetThing(args, source)
        }
    }

    private suspend fun tryAndGetThing(args: List<String>, source: Player) {
        val thing = getThing(args, source)
        when {
            thing == null -> source.displayToMe("Couldn't find ${args.joinToString(" ")}.")
            thing.isLookingAtBody() -> EventManager.postEvent(LookEvent(source, thing.thing, body = thing.thing.body))
            thing.bodyPartThings.firstOrNull() != null -> EventManager.postEvent(LookEvent(source, location = thing.bodyPartThings.firstOrNull()))
            else -> EventManager.postEvent(LookEvent(source, thing = thing.thing))
        }
    }

    private suspend fun getThing(args: List<String>, source: Player): ThingAim? {
        val allThings = source.thing.currentLocation().getThingsIncludingInventories()
        val things = parseThings(source.thing, args, allThings)

        return when {
            things.size == 1 -> things.first()
            things.isEmpty() -> {
                clarifyThing(source)
                null
            }
            else -> {
                clarifyThings(source, things)
                null
            }
        }
    }

    private suspend fun clarifyThing(source: Player) {
        source.respondSuspend({}) {
            message("Look at what?")
            options(listOf("all") + source.thing.currentLocation().getThings().map { it.name })
            command { "look $it" }
        }
    }

    private fun clarifyThings(source: Player, options: List<ThingAim>) {
        source.respond("There isn't anything to look at.") {
            message("Look at what?")
            options(options.map { it.toString() })
            command { "look $it" }
        }
    }

}