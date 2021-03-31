package wiki

import core.commands.Command
import core.commands.CommandParser
import java.io.File

private const val filePath = "../quest_command.wiki/Commands.md"

internal fun printCommands() {
    val existing = getExistingText(filePath, "## Command List")
    val tables = generateTables()

    File(filePath).printWriter().use { out ->
        out.println(existing)
        out.println(tables)
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
        
        Command | Aliases | Details <img width=1000/>
         --- | --- | ---  
        """.trimIndent()

    val rows = commands.joinToString("\n") { command ->
        "\n ${command.name} | ${command.getAliases().joinToString("<br/>")} | ${prepareDescription(command.getDescription())} <br/> ${prepareManual(command.getManual())}"
//        "\n **${command.name}** <br/><br/> (${command.getAliases().joinToString(", ")}) | ${prepareDescription(command.getDescription())} <br/> ${prepareManual(command.getManual())}"
    }.replace("\n\n", "\n")
    return tableHeader + rows
}

