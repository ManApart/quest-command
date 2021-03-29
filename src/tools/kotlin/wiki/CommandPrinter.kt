package wiki

import core.commands.Command
import core.commands.CommandParser
import java.io.File

private val filePath = "../quest_command.wiki/Commands.md"

internal fun printCommands() {
    val existing = getExistingText(filePath)
    val table = generateTables()

    File(filePath).printWriter().use { out ->
        out.println(existing)
        out.println(table)
    }
}

private fun generateTables(): String {
    return CommandParser.getGroupedCommands().entries.joinToString("\n\n") { (category, commands) ->
        generateTable(category, commands)
    }
}

private fun generateTable(category: String, commands: List<Command>): String {
    val tableHeader = """
        ### $category
        
        Command | Aliases | Description | Usages 
         --- | --- | --- | --- 
        """.trimIndent()

    val rows = commands.joinToString("\n") { command ->
        "\n ${command.name} | ${command.getAliases().joinToString(", ")} | ${prepareDescription(command.getDescription())} | ${prepareManual(command.getManual())}"
    }.replace("\n\n", "\n")
    return tableHeader + rows
}

private fun getExistingText(filePath: String): String {
    val fullExisting = File(filePath).readText()
    val searchText = "## Command List"
    val i = fullExisting.indexOf(searchText)

    return fullExisting.substring(0, i + searchText.length) + "\n"
}