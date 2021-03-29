package wiki

import core.DependencyInjector
import magic.spellCommands.SpellCommand
import magic.spellCommands.SpellCommandsCollection


internal fun printSpellCommands() {
    var spellTableString = "\nSpell Commands:" +
            "\n\nCategory | Command | Description | Usages" +
            "\n --- | --- | --- | --- "
    getGroupedSpellCommands().forEach { (category, commands) ->
        commands.forEach { command ->
            spellTableString += "\n $category | ${command.name} | ${prepareDescription(command.getDescription())} | ${prepareManual(command.getManual())}"
        }
    }

    println(spellTableString)
}

private fun getGroupedSpellCommands(): Map<String, List<SpellCommand>> {
    val groups = HashMap<String, MutableList<SpellCommand>>()
    DependencyInjector.getImplementation(SpellCommandsCollection::class.java).values.forEach { command ->
        run {
            if (!groups.containsKey(command.getCategory()[0])) {
                groups[command.getCategory()[0]] = ArrayList()
            }
            groups[command.getCategory()[0]]?.add(command)
        }
    }
    groups.forEach { entry ->
        entry.value.sortBy { it.name }
    }
    return groups.toSortedMap()
}