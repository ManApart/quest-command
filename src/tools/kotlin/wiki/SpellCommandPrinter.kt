package wiki

import core.DependencyInjector
import magic.spellCommands.SpellCommand
import magic.spellCommands.SpellCommandsCollection
import java.io.File

private const val filePath = "../wiki/systems/magic/Spell_Commands.md"

internal fun printSpellCommands() {
    val existing = getExistingText(filePath, "## Command List")
    val table = generateTable()

    File(filePath).printWriter().use { out ->
        out.println(existing)
        out.println(table)
    }
}

private fun generateTable(): String {
    var spellTableString = "Category | Command | Description | Usages" +
            "\n --- | --- | --- | --- "
    getGroupedSpellCommands().forEach { (category, commands) ->
        commands.forEach { command ->
            spellTableString += "\n $category | ${command.name} | ${prepareDescription(command.getDescription())} | ${prepareManual(command.getManual())}"
        }
    }
    return spellTableString
}


private fun getGroupedSpellCommands(): Map<String, List<SpellCommand>> {
    val groups = HashMap<String, MutableList<SpellCommand>>()
    DependencyInjector.getImplementation(SpellCommandsCollection::class).values.forEach { command ->
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