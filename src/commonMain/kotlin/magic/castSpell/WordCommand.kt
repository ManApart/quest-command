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
import traveling.position.ThingAim

class WordCommand : Command() {
    private val spellCommands by lazy { loadSpellCommands() }

    private fun loadSpellCommands(): NameSearchableList<SpellCommand> {
        return NameSearchableList(DependencyInjector.getImplementation(SpellCommandsCollection::class).values)
    }

    override fun getAliases(): List<String> {
        return listOf("Word")
    }

    override fun getDescription(): String {
        return "Consider words of power."
    }

    override fun getManual(): String {
        return """
    word list - list known words. 
    word <word> - view the manual for that word.
    """
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("list") + spellCommands.flatMap { it.getCategory() + listOf(it.name) }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        if (args.size <= 1) {
            if (args.isEmpty() || args.first() == "list") {
                EventManager.postEvent(ViewWordHelpEvent(source.thing))
            } else if (isCategory(args)) {
                EventManager.postEvent(ViewWordHelpEvent(source.thing, args.first(), true))
            } else {
                EventManager.postEvent(ViewWordHelpEvent(source.thing, args.first()))
            }
        } else {
            source.thing.displayToMe("Unknown command: ${args.joinToString(" ")}")
        }
    }

    private fun isCategory(args: List<String>): Boolean {
        val word = args.first().lowercase()
        val categories = spellCommands.map { command ->
            command.getCategory().map { category ->
                category.lowercase()
            }
        }.flatten()
        return categories.contains(word)
    }
}