package interact.magic

import core.commands.parseTargets
import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.history.display
import core.utility.NameSearchableList
import core.utility.reflection.Reflections
import interact.magic.spellCommands.SpellCommand
import system.DependencyInjector
import system.EventManager

class CastCommand : Command() {
    private var reflections = DependencyInjector.getImplementation(Reflections::class.java)
    private val spellCommands by lazy { loadSpellCommands() }

    private fun loadSpellCommands(): NameSearchableList<SpellCommand> {
        return NameSearchableList(reflections.getSpellCommands())
    }

    override fun getAliases(): Array<String> {
        return arrayOf("Cast", "word")
    }

    override fun getDescription(): String {
        return "Cast:\n\tSpeak a word of power."
    }

    override fun getManual(): String {
        return "\n\tword list - list known words." +
                "\n\tword <word> - view the manual for that word." +
                "\n\tCast <word> <word args> on *<target>" +
                "\n\tSimple Example:" +
                "\n\t\t'Cast shard 5 on bandit'. This would cast an ice shard with 5 points of damage at a random body part of the bandit." +
                "\n\tComplicated Example:" +
                "\n\t\t'Cast shard 5 on left arm chest of bandit and head of rat'. This would cast an ice shard with five damage at the left arm of the bandit, another at the bandit's chest, and a third at the head of the rat."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        when (keyword) {
            "word" -> executeWord(args)
            else -> castWord(args)
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

    private fun castWord(args: List<String>) {
        if (args.isEmpty()) {
            //TODO -request response what to cast etc
            CommandParser.parseCommand("help cast")
        } else {
            val spellCommand = getSpellCommand(args)
            if (spellCommand != null) {
                val arguments = Args(args, delimiters = listOf("on"))
                val spellArgs = parseSpellArgs(arguments)
                val targets = parseTargets("cast ${spellCommand.name} ${spellArgs.fullString}", arguments.getGroup(1))
                spellCommand.execute(spellArgs, targets)
            } else {
                display("Unknown word ${args.first()}")
            }
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
        val words = args.getGroup(0)
        if (words.size > 1) {
            return Args(words.subList(1, words.size))
        }
        return Args(listOf())
    }
}