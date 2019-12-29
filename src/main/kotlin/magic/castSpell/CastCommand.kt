package magic.castSpell

import combat.battle.position.TargetAim
import core.commands.*
import core.target.Target
import core.body.BodyPart
import core.history.display
import core.utility.NameSearchableList
import core.reflection.Reflections
import magic.spellCommands.SpellCommand
import core.DependencyInjector
import core.events.EventManager
import magic.ViewWordHelpEvent

class CastCommand : Command() {
    private var reflections = DependencyInjector.getImplementation(Reflections::class.java)
    //Should this go to the command parser?
    private val spellCommands by lazy { loadSpellCommands() }

    private fun loadSpellCommands(): NameSearchableList<SpellCommand> {
        return NameSearchableList(reflections.getSpellCommands())
    }

    override fun getAliases(): Array<String> {
        return arrayOf("Cast", "word", "c")
    }

    override fun getDescription(): String {
        return "Cast:\n\tSpeak a word of power."
    }

    override fun getManual(): String {
        return "\n\tword list - list known words." +
                "\n\tword <word> - view the manual for that word." +
                "\n\tCast <word> <word args> on *<target> - cast a spell with specific arguments on a target" +
                "\n\tSimple Example:" +
                "\n\t\t'Cast shard 5 on bandit'. This would cast an ice shard with 5 points of damage at a random body part of the bandit." +
                "\n\tComplicated Example:" +
                "\n\t\t'Cast shard 5 on left arm chest of bandit and head of rat'. This would cast an ice shard with five damage at the left arm of the bandit, another at the bandit's chest, and a third at the head of the rat."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    fun hasWord(keyword: String): Boolean {
        return spellCommands.exists(keyword)
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        when (keyword) {
            "word" -> executeWord(args)
            else -> castWord(source, keyword != "cast", args, keyword == "c")
        }
    }

    private fun executeWord(args: List<String>) {
        if (args.size <= 1) {
            if (args.isEmpty() || args.first() == "list") {
                EventManager.postEvent(ViewWordHelpEvent())
            } else if (isCategory(args)) {
                EventManager.postEvent(ViewWordHelpEvent(args.first(), true))
            } else {
                EventManager.postEvent(ViewWordHelpEvent(args.first()))
            }
        } else {
            display("Unknown command: ${args.joinToString(" ")}")
        }
    }

    private fun isCategory(args: List<String>): Boolean {
        val word = args.first().toLowerCase()
        val categories = spellCommands.map { command ->
            command.getCategory().map { category ->
                category.toLowerCase()
            }
        }.flatten()
        return categories.contains(word)
    }

    private fun castWord(source: Target, isAlias: Boolean, args: List<String>, useDefaults: Boolean) {
        if (args.isEmpty()) {
            clarifyWord()
        } else {
            val spellCommand = getSpellCommand(args)
            if (spellCommand != null) {
                val arguments = Args(args, delimiters = listOf("on"))
                val spellArgs = parseSpellArgs(arguments)
                val targets = parseTargets(arguments.getGroup("on")).toMutableList()
                if (isAlias && targets.isEmpty()) {
                    targets.add(TargetAim(source, parseBodyParts(source, args)))
                }
                spellCommand.execute(source, spellArgs, targets, useDefaults)
            } else {
                display("Unknown word ${args.first()}")
            }
        }
    }

    private fun clarifyWord() {
        val options = spellCommands.map { it.name }
        val message = "Cast what?\n\t${options.joinToString(", ")}"
        val response = ResponseRequest(message, options.map { it to "cast $it" }.toMap())
        CommandParser.setResponseRequest(response)
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

fun getTargetedPartsOrAll(targetAim: TargetAim, maxParts: Int = -1): List<BodyPart> {
    val parts =  if (targetAim.bodyPartTargets.isNotEmpty()) {
        targetAim.bodyPartTargets
    } else {
        targetAim.target.body.getParts()
    }

    return if (maxParts > 0){
        parts.take(maxParts)
    } else {
        parts
    }

}

fun getTargetedPartsOrRootPart(targetAim: TargetAim): List<BodyPart> {
    return if (targetAim.bodyPartTargets.isNotEmpty()) {
        targetAim.bodyPartTargets
    } else {
        listOfNotNull(targetAim.target.body.getRootPart() ?: targetAim.target.body.getParts().firstOrNull())
    }
}