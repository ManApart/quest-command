package test

import core.commands.CommandParser
import magic.spellCommands.SpellCommand
import core.DependencyInjector
import magic.spellCommands.SpellCommandsCollection

private val newLineAtLineStart = Regex("^\n")

fun main() {
    printCommands()
    printSpellCommands()
}


private fun printCommands() {
    var tableString = "Category | Command | Aliases | Description | Usages" +
            "\n --- | --- | --- | --- | --- "
    CommandParser.getGroupedCommands().forEach { (category, commands) ->
        commands.forEach { command ->
            tableString += "\n $category | ${command.name} | ${command.getAliases().joinToString(", ")} | ${prepareDescription(command.getDescription())} | ${prepareManual(command.getManual())}"
        }
    }
    println(tableString)
}

private fun printSpellCommands() {
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


private fun prepareDescription(description: String): String {
    return prepareLines(description.substringAfter(":\n"))
}

private fun prepareManual(manual: String): String {
    return manual.split("\n").joinToString("<br/>") { line ->
        val dashes = line.split("-")
        if (dashes.size == 1) {
            line
        } else {
            val command = "`" + dashes[0].trim() + "`"
            val explanation = prepareLines(dashes[1])
            "$command - $explanation"
        }
    }

}

private fun prepareLines(lines: String): String {
    return lines
            .replace(newLineAtLineStart, "")
            .replace("<", "\\<")
            .replace("\n", "<br/>")
            .replace("\t", "")
            .trim()
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