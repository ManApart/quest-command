package magic.castSpell

import core.DependencyInjector
import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import core.utility.NameSearchableList
import magic.ViewWordHelpEvent
import magic.spellCommands.SpellCommand
import magic.spellCommands.SpellCommandsCollection
import traveling.location.location.Location
import traveling.position.ThingAim

class CastCommand : Command() {
    private val spellCommands by lazy { loadSpellCommands() }

    private fun loadSpellCommands(): NameSearchableList<SpellCommand> {
        return NameSearchableList(DependencyInjector.getImplementation(SpellCommandsCollection::class).values)
    }

    override fun getAliases(): List<String> {
        return listOf("Cast", "word", "c")
    }

    override fun getDescription(): String {
        return "Speak a word of power."
    }

    override fun getManual(): String {
        return """
    cast <word> <word args> on *<thing> - cast a spell with specific arguments on a thing
    Simple Example:
        Cast shard 5 on bandit - This would cast an ice shard with 5 points of damage at a random body part of the bandit.
    Complicated Example:
        Cast shard 5 on left arm chest of bandit and head of rat - This would cast an ice shard with five damage at the left arm of the bandit, another at the bandit's chest, and a third at the head of the rat."""
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    fun hasWord(keyword: String): Boolean {
        return spellCommands.exists(keyword)
    }

    override fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> spellCommands.map { it.name }
            spellCommands.exists(args.first()) -> spellCommands.get(args.first()).suggest(source, args.first(), args.subList(1, args.size))
            else -> listOf()
        }
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val isAlias = keyword != "cast"
        val useDefaults = keyword == "c"
        if (args.isEmpty()) {
            clarifyWord(source)
        } else {
            val spellCommand = getSpellCommand(args)
            if (spellCommand != null) {
                val arguments = Args(args, delimiters = listOf("on"))
                val spellArgs = parseSpellArgs(arguments)
                val things = parseThingsFromLocation(source.thing, arguments.getGroup("on")).toMutableList()
                if (isAlias && things.isEmpty()) {
                    things.add(ThingAim(source.thing, parseBodyParts(source.thing, args)))
                }
                spellCommand.execute(source, spellArgs, things, useDefaults)
            } else {
                source.displayToMe("Unknown word ${args.first()}")
            }
        }
    }

    private fun clarifyWord(source: Player) {
        source.respond("There are no spells you can cast.") {
            message("Cast what?")
            optionsNamed(spellCommands)
            command { "cast $it" }
        }
    }

    private fun getSpellCommand(args: List<String>): SpellCommand? {
        return if (spellCommands.exists(args.first())) {
            spellCommands.get(args.first())
        } else {
            null
        }
    }

    private fun parseSpellArgs(args: Args): Args {
        val words = args.getBaseGroup()
        if (words.size > 1) {
            return Args(words.subList(1, words.size))
        }
        return Args(listOf())
    }
}

fun getThingedPartsOrAll(thingAim: ThingAim, maxParts: Int = -1): List<Location> {
    val parts = thingAim.bodyPartThings.ifEmpty {
        thingAim.thing.body.getParts()
    }

    return if (maxParts > 0) {
        parts.take(maxParts)
    } else {
        parts
    }

}

fun getThingedPartsOrRootPart(thingAim: ThingAim): List<Location> {
    return thingAim.bodyPartThings.ifEmpty {
        listOfNotNull(thingAim.thing.body.getRootPart())
    }
}