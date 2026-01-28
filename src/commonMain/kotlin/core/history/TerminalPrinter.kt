package core.history

import core.GameState
import core.Player
import core.commands.CommandParsers
import system.debug.DebugType

object TerminalPrinter {
    private var lastFlushed = 0

    fun reset() {
        lastFlushed = 0
    }

    fun print(player: Player) {
        val history = GameLogger.getMainHistory()
        history.history.subList(lastFlushed, history.history.size).forEach { io ->
            io.outPut.forEach { line ->
                println(line)
            }
        }
        optionsToPrintIfTheyExist(player)?.let { println(it.joinToString(", ")) }

        if (GameState.getDebugBoolean(DebugType.SHOW_PROMPT)) print("\n> ")
        lastFlushed = history.history.size
    }

    fun optionsToPrintIfTheyExist(player: Player): List<String>? {
        return CommandParsers.getParser(player).getResponseRequest()?.getOptions()?.let { options ->
            options.mapIndexed { i, op -> "${i + 1}) $op" }
        }
    }
}
