package interact.magic

import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.gameState.Target
import core.history.display
import core.utility.NameSearchableList
import core.utility.reflection.Reflections
import interact.magic.spellCommands.SpellCommand
import interact.scope.ScopeManager
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
                "\n\tCast <word> on *<target> and *<target>" +
                "\n\tCast <word> <word args> on *<body part> of *<target>. NOT IMPLEMENTED"
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
        val spellCommand = getSpellCommand(args)
        if (spellCommand != null) {
            val arguments = Args(args, delimiters = listOf("on"))
            val targets = parseTargets(arguments)
            val spellArgs = parseSpellArgs(arguments)
            spellCommand.execute(spellArgs, targets)
        } else {
            display("Unknown word ${args.first()}")
        }
    }

    private fun getSpellCommand(args: List<String>): SpellCommand? {
        return if (spellCommands.exists(args.first())) {
            spellCommands.get(args.first())
        } else {
            null
        }
    }

    private fun parseSpellArgs(args: Args): List<String> {
        val words = args.getGroup(0)
        if (words.size > 1) {
            return words.subList(1, words.size)
        }
        return listOf()
    }

    private fun parseTargets(args: Args): List<Target> {
        val targetWords = args.getGroup(1)
        if (targetWords.isNotEmpty()){
            val targetArgs = Args(targetWords, delimiters = listOf("and"))
            return targetArgs.argStrings.map { getTarget(it) }.flatten()
        }
        return listOf()
    }

    private fun getTarget(name: String) : List<Target> {
        return ScopeManager.getScope(GameState.player.location).getTargets(name)
    }
}