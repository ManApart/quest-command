package validation

import core.commands.Command
import core.commands.CommandParser.commands
import core.commands.CommandParser.getCategories
import core.commands.UnknownCommand

class CommandValidator {
    private val categories = getCategories()

    fun validate(): Int {
        return noAliasCategoryOverlap() +
                noAliasDuplicates()
    }

    private fun noAliasCategoryOverlap(): Int {
        val suspectCategories = mutableListOf<String>()
        var warnings = 0

        commands.forEach { command ->
            command.getAliases().forEach {
                if (categories.contains(it) && command.javaClass.simpleName != UnknownCommand::class.java.simpleName) {
                    println("WARN: Alias '$it' from ${command.javaClass.simpleName} is a category")
                    suspectCategories.add(it)
                    warnings++
                }
            }
        }

        suspectCategories.forEach { suspect ->
            commands.forEach { command ->
                if (command.getCategory().contains(suspect)) {
                    println("${command.javaClass.simpleName} is in category $suspect")
                }
            }
        }
        return warnings
    }

    private fun noAliasDuplicates(): Int {
        var warnings = 0
        val aliases = mutableMapOf<String, Command>()
        commands.forEach { command ->
            command.getAliases().forEach {
                if (aliases.containsKey(it)) {
                    println("WARN: Alias '$it' exists for both ${command.javaClass.simpleName} and ${aliases[it]?.javaClass?.simpleName}")
                    warnings++
                } else {
                    aliases[it] = command
                }
            }
        }
        return warnings
    }


}