package core.history

import core.GameState
import core.PRINT_WITHOUT_FLUSH
import core.target.Target

class ChatHistory(val listener: Target) {
    val history = mutableListOf<InputOutput>()
    private var current: InputOutput? = null
    private var lastFlushed = 0

    fun addInput(input: String) {
        if (current != null) history.add(current!!)
        current = InputOutput(input)
    }

    fun print(message: String) {
        if (current == null) current = InputOutput()
        current!!.outPut.add(message)
        if (GameState.properties.values.getBoolean(PRINT_WITHOUT_FLUSH)) println(message)
    }

    fun reset() {
        history.clear()
        current = InputOutput()
        lastFlushed = 0
    }

    fun getLastInput(): String {
        return getCurrent().input ?: ""
    }

    fun getLastOutput(): String {
        return getCurrent().outPut.lastOrNull() ?: ""
    }

    fun getLastOutputs(): List<String> {
        return getCurrent().outPut
    }

    fun getCurrent(): InputOutput {
        return current ?: history.lastOrNull() ?: InputOutput()
    }

    fun flush() {
        if (current != null) history.add(current!!)
        current = null
        history.subList(lastFlushed, history.size).forEach { io ->
            io.outPut.forEach { line ->
                println(line)
            }
        }
        lastFlushed = history.size
    }

}