@file:JvmName("QuestCommand")

import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.TerminalPrinter
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        GameManager.newOrLoadGame()
        EventManager.executeEvents()
        if (args.map { it.lowercase().replace("-", "") }.contains("windowed")) {
            runInGui()
        } else {
            runInTerminal()
        }
    }
}

private suspend fun runInTerminal() {
    CommandParsers.parseInitialCommand(GameState.player)
    TerminalPrinter.print()
    while (GameManager.playing) {
        CommandParsers.parseCommand(GameState.player, readlnOrNull() ?: "")
        TerminalPrinter.print()
    }
}