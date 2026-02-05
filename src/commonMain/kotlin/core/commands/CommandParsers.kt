package core.commands

import conversation.Conversation
import conversation.ConversationCommandInterceptor
import core.DependencyInjector
import core.GameState
import core.Player
import core.utility.*
import magic.castSpell.CastCommand

object CommandParsers {
    private var commandsCollection = DependencyInjector.getImplementation(CommandsCollection::class)
    var commands = loadCommands()
    val unknownCommand by lazy { commands.first { it::class == UnknownCommand::class } as UnknownCommand }
    val castCommand by lazy { commands.first { it::class == CastCommand::class } as CastCommand }
    private val parsers = mutableMapOf<String, CommandParser>()
    private val commandNameList by lazy { commands.map { it.getAliases().first().lowercase() }.filter { it.isNotBlank() } }

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

    fun getParser(player: Player): CommandParser {
        if (!parsers.containsKey(player.name)) {
            parsers[player.name] = CommandParser(player)
        }
        return parsers[player.name]!!
    }

    fun getConversations(): List<Conversation> {
        return parsers.values.mapNotNull { it.commandInterceptor }.filter { it is ConversationCommandInterceptor }.map { it as ConversationCommandInterceptor }.map { it.conversation }
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

    suspend fun parseInitialCommand(player: Player) {
        parsers[player.name]?.parseInitialCommand()
    }

    suspend fun parseCommand(player: Player, line: String) {
        parseCommand(line, player.name)
    }

    suspend fun parseCommand(line: String, name: String) {
        parsers[name]?.parseCommand(line)
    }

    fun setResponseRequest(player: Player, responseRequest: ResponseRequest?) {
        parsers[player.name]?.setResponseRequest(responseRequest)
    }

    fun cleanLine(line: String): List<String> {
        return line.lowercase().split(" ").asSequence().map { it.trim() }.filter { it.isNotBlank() }.toList()
    }

    fun isCommand(input: String) = commandNameList.contains(input)

    suspend fun suggestions(player: Player, args: String): List<String> {
        val input = cleanLine(args)
        val firstInput = input.first()
        return when {
            (input.isEmpty() || input.all { it.isBlank() }) -> commandNameList

            commandNameList.contains(firstInput) -> {
                val cleanedArgs = if (args.endsWith(" ")) input.removeFirstItem() else input.removeFirstItem().removeLastItem()
                val suggestions = findCommand(firstInput).suggest(player, firstInput, cleanedArgs)
                if (input.size > 1) {
                    suggestions.filter { it.lowercase().contains(input.last()) }
                } else suggestions
            }

            commandNameList.any { it.startsWith(firstInput) } -> commandNameList.filter { it.startsWith(firstInput) }.also { println("starts with: $it") }

            else -> emptyList()
        }.map { it.capitalize2() }.sorted().toSet().toList()
    }
}
