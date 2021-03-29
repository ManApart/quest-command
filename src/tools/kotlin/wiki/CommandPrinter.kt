package wiki

import core.commands.CommandParser
import java.io.File

private val filePath = "../quest_command.wiki/Commands.md"

internal fun printCommands() {
    val existing = getExistingText(filePath)
    val table = generateTable()

    File(filePath).printWriter().use { out ->
        out.println(existing)
        out.println(table)
    }
}

private fun generateTable() : String{
    var tableString = "Category | Command | Aliases | Description | Usages" +
            "\n --- | --- | --- | --- | --- "
    CommandParser.getGroupedCommands().forEach { (category, commands) ->
        commands.forEach { command ->
            tableString += "\n $category | ${command.name} | ${command.getAliases().joinToString(", ")} | ${prepareDescription(command.getDescription())} | ${prepareManual(command.getManual())}"
        }
    }
    return tableString
}

private fun getExistingText(filePath: String) : String {
    val fullExisting = File(filePath).readText()
    val searchText = "## Command List"
    val i = fullExisting.indexOf(searchText)

    return fullExisting.substring(0, i + searchText.length)
}