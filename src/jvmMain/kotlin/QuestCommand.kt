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
    GameState.player.displayToMe("Stuff")
    CommandParsers.parseInitialCommand(GameState.player)
    GameState.player.displayToMe("Stuff2")
    TerminalPrinter.print()
    GameState.player.displayToMe("Stuff3")
    while (GameManager.playing) {
        CommandParsers.parseCommand(GameState.player, readlnOrNull() ?: "")
        TerminalPrinter.print()
    }
}