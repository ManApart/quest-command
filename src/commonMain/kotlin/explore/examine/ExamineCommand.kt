package explore.examine

import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import traveling.position.ThingAim

class ExamineCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Examine", "Exa", "cat", "describe")
    }

    override fun getDescription(): String {
        return "Examine your surroundings in detail."
    }

    override fun getManual(): String {
        return """
	Examine all - Look more closely at your surroundings. Gives more detailed information than look, based on how perceptive you are.
	Examine <thingAim> - Look closely at a specific thing.
    Examine body of <thing> - Look closely at the body of a thing.
    Examine mind of <thing> - Ponder the thought of a creature. This requires the utmost perception and wisdom.
    Examine hand of player - Look closely at the player's right hand.
    """
    }

    override fun getCategory(): List<String> {
        return listOf("Explore")
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        when {
            keyword == "examine" && args.isEmpty() -> clarifyThing(source)
            args.isEmpty() -> EventManager.postEvent(ExamineEvent(source))
            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ExamineEvent(source))
            else -> tryAndGetThing(args, source)
        }
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("all", "body", "mind", "hand") + source.getPerceivedThingNames()
            args.last() in listOf("body", "mind", "hand") -> listOf("of")
            args.last() == "of" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    private suspend fun tryAndGetThing(args: List<String>, source: Player) {
        var lookingAtMind = false
        val thing = if (args.contains("mind") && args.contains("of")) {
            lookingAtMind = true
            getThing(args.removeAll(listOf("mind", "of")), source)
        } else getThing(args, source)

        when {
            thing == null -> source.displayToMe("Couldn't find ${args.joinToString(" ")}.")
            lookingAtMind -> EventManager.postEvent(ExamineEvent(source, thing.thing, mind = thing.thing.mind))
            thing.isLookingAtBody() -> EventManager.postEvent(ExamineEvent(source, thing.thing, body = thing.thing.body))
            thing.bodyPartThings.firstOrNull() != null -> EventManager.postEvent(ExamineEvent(source, location = thing.bodyPartThings.firstOrNull()))
            else -> EventManager.postEvent(ExamineEvent(source, thing = thing.thing))
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
            message("Examine what?")
            options(listOf("all") + source.thing.currentLocation().getThings().map { it.name })
            command { "examine $it" }
        }
    }

    private fun clarifyThings(source: Player, options: List<ThingAim>) {
        source.respond("There isn't anything to examine.") {
            message("Examine what?")
            options(options.map { it.toString() })
            command { "examine $it" }
        }
    }

}