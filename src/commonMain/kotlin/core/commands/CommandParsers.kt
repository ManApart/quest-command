package core.commands

import core.DependencyInjector
import core.GameState
import core.Player
import core.utility.NameSearchableList
import core.utility.toSortedMap
import magic.castSpell.CastCommand

object CommandParsers {
    private var commandsCollection = DependencyInjector.getImplementation(CommandsCollection::class)
    var commands = loadCommands()
    val unknownCommand by lazy { commands.first { it::class == UnknownCommand::class } as UnknownCommand }
    val castCommand by lazy { commands.first { it::class == CastCommand::class } as CastCommand }
    private val parsers = mutableMapOf<String, CommandParser>()

    init {
        GameState.players.values.forEach { addParser(it) }
    }

    fun reset() {
        parsers.clear()
        commands = loadCommands()
        GameState.players.values.forEach { addParser(it) }
    }

    fun addParser(player: Player) {
        parsers[player.name] = CommandParser(player)
    }

    fun getParser(player: Player) : CommandParser {
        if (!parsers.containsKey(player.name)) {
            parsers[player.name] = CommandParser(player)
        }
        return parsers[player.name]!!
    }

    private fun loadCommands(): NameSearchableList<Command> {
        val commands = NameSearchableList(commandsCollection.values.toList())

        commands.forEach {
            commands.addProxy(it, it.getAliases().toList())
        }

        return commands
    }

    fun findCommand(alias: String): Command {
        return commands.getOrNull(alias) ?: unknownCommand
    }

    inline fun <reified C : Command> getCommand(): C {
        return commands.first { it is C } as C
    }

    fun getCategories(): List<String> {
        val categories = mutableListOf<String>()
        commands.flatMap { it.getCategory() }.forEach {
            if (!categories.contains(it)) {
                categories.add(it)
            }
        }
        return categories
    }

    fun getGroupedCommands(): Map<String, List<Command>> {
        val groups = HashMap<String, MutableList<Command>>()
        commands.forEach { command ->
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

    fun parseInitialCommand(player: Player, args: Array<String>) {
        parseInitialCommand(args, player.name)
    }

    //TODO - delete these and just use player version
    fun parseInitialCommand(args: Array<String>, name: String) {
        parsers[name]?.parseInitialCommand(args)
    }

    fun parseCommand(player: Player, line: String) {
        parseCommand(line, player.name)
    }

    fun parseCommand(line: String, name: String) {
        parsers[name]?.parseCommand(line)
    }

    fun setResponseRequest(player: Player, responseRequest: ResponseRequest?) {
        parsers[player.name]?.setResponseRequest(responseRequest)
    }

    fun cleanLine(line: String): List<String> {
        return line.lowercase().split(" ").asSequence().map { it.trim() }.filter { it.isNotEmpty() }.toList()
    }

}
