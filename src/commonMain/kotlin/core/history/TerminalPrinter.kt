package core.history

import core.GameState
import system.debug.DebugType

object TerminalPrinter {
    private var lastFlushed = 0

    fun reset(){
        lastFlushed = 0
    }

    fun print() {
        val history = GameLogger.getMainHistory()
        history.history.subList(lastFlushed, history.history.size).forEach { io ->
            io.outPut.forEach { line ->
                println(line)
            }
        }
        if(GameState.getDebugBoolean(DebugType.SHOW_PROMPT)) print("\n> ")
        lastFlushed = history.history.size
    }
}
