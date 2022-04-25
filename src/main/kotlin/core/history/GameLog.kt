package core.history

import core.GameState
import core.PRINT_WITHOUT_FLUSH
import core.Player

class GameLog(val listener: Player) {
    val history = mutableListOf<InputOutput>()
    private var current: InputOutput? = null

    override fun toString(): String {
        return "id: ${listener.name} name: ${listener.thing.name} size: ${history.size}"
    }

    override fun equals(other: Any?): Boolean {
        return other is GameLog && listener.name == other.listener.name
    }

    override fun hashCode(): Int {
        return current?.hashCode() ?: 0
    }

    fun addInput(input: String) {
        current = InputOutput(input)
    }

    fun endCurrent() {
        if (current != null) history.add(current!!)
    }

    fun print(message: String) {
        if (current == null) current = InputOutput()
        current!!.outPut.add(message)
        if (GameState.properties.values.getBoolean(PRINT_WITHOUT_FLUSH) && GameLogger.getMainHistory() == this) println(message)
    }

    fun reset() {
        history.clear()
        current = InputOutput()
    }

    fun getLastInput(): String {
        return getCurrent().input
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

    fun contains(line: String): Boolean {
        return current?.outPut?.contains(line) ?: false || history.any { history -> history.outPut.any { it.contains(line) } }
    }

}