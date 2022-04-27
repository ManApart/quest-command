package core.history

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
        lastFlushed = history.history.size
    }
}