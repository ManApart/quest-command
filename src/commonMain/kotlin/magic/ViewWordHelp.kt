package magic

import core.DependencyInjector
import core.events.EventListener
import core.history.displayToMe
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.toSortedMap
import magic.spellCommands.SpellCommand
import magic.spellCommands.SpellCommandsCollection

class ViewWordHelp : EventListener<ViewWordHelpEvent>() {
    private val wordsOfPower by lazy { loadSpellCommands() }

    private fun loadSpellCommands(): NameSearchableList<SpellCommand> {
        return NameSearchableList(DependencyInjector.getImplementation(SpellCommandsCollection::class).values)
    }

    override suspend fun execute(event: ViewWordHelpEvent) {
        when {
            event.word == null -> listWords(event.source)
            event.groups -> printWordGroup(event.source, event.word.lowercase())
            else -> helpWord(event.source, event.word.lowercase())
        }
    }

    private fun listWords(source: Thing) {
        val groups = HashMap<String, MutableList<String>>()
        wordsOfPower.forEach { word ->
            run {
                if (!groups.containsKey(word.getCategory()[0])) {
                    groups[word.getCategory()[0]] = ArrayList()
                }
                groups[word.getCategory()[0]]?.add(word.name)
            }
        }
        var groupList = ""
        groups.toSortedMap().forEach {
            it.value.sort()
            groupList += "${it.key}:\n\t${it.value.joinToString(", ")}\n"
        }
        source.displayToMe("Word <Group Name> to learn about one of the following groups:\n$groupList")
    }

    private fun printWordGroup(source: Thing, group: String) {
        var description = "Word <Word> to learn more about on of the following topics:\n"
        wordsOfPower.forEach { word ->
            if (word.getCategory().map { it.lowercase() }.contains(group)) {
                description += word.name + "\n\t" + word.getDescription() + "\n"
            }
        }
        source.displayToMe(description)
    }

    private fun helpWord(source: Thing, word: String) {
        if (wordsOfPower.exists(word)) {
            source.displayToMe(wordsOfPower.get(word).getManual())
        } else {
            source.displayToMe("Unknown word of power: $word")
        }
    }


    private fun isCategory(args: List<String>): Boolean {
        return wordsOfPower.asSequence().map { command -> command.getCategory().map { category -> category.lowercase() } }.contains(args)
    }

    private fun isWordOfPower(args: List<String>) =
            args.size == 1 && wordsOfPower.exists(args[0])

    private fun getGroups(): List<String> {
        return wordsOfPower.map { it.getCategory() }.flatten().distinct()
    }

    private fun getCommands(group: String): List<SpellCommand> {
        val cleanGroup = group.trim().lowercase()
        return wordsOfPower.filter { word ->
            word.getCategory().map { category ->
                category.lowercase()
            }.contains(cleanGroup)
        }
    }

}