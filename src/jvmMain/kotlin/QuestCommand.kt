@file:JvmName("QuestCommand")

import building.loadMods
import core.GameManager
import core.GameState
import core.commands.CommandParsers
import core.events.EventManager
import core.history.TerminalPrinter
import core.history.displayToMe
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    runBlocking {
        loadMods()
        GameManager.newOrLoadGame()
        EventManager.processEvents()
        if (args.map { it.lowercase().replace("-", "") }.contains("windowed")) {
            runInGui()
        } else {
            runInTerminal()
        }
    }
}

private suspend fun runInTerminal() {
    CommandParsers.parseInitialCommand(GameState.player)
    TerminalPrinter.print(GameState.player)
    while (GameManager.playing) {
        CommandParsers.parseCommand(GameState.player, readlnOrNull() ?: "")
        TerminalPrinter.print(GameState.player)
    }
}