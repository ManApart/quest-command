package validation

import core.commands.Command
import core.commands.CommandParsers.commands
import core.commands.CommandParsers.getCategories
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
        val cleanedCategories = categories.map { it.lowercase() }

        commands.forEach { command ->
            command.getAliases().map { it.lowercase() }.forEach {
                if (cleanedCategories.contains(it) && command::class.simpleName != UnknownCommand::class.simpleName) {
                    println("WARN: Alias '$it' from ${command::class.simpleName} is a category")
                    suspectCategories.add(it)
                    warnings++
                }
            }
        }

        suspectCategories.forEach { suspect ->
            commands.forEach { command ->
                if (command.getCategory().contains(suspect)) {
                    println("${command::class.simpleName} is in category $suspect")
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
                    println("WARN: Alias '$it' exists for both ${command::class.simpleName} and ${aliases[it]!!::class.simpleName}")
                    warnings++
                } else {
                    aliases[it] = command
                }
            }
        }
        return warnings
    }


}