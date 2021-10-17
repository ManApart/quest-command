package core.history

import core.GameState
import core.PRINT_WITHOUT_FLUSH
import core.Player

class GameLog(val listener: Player) {
    val history = mutableListOf<InputOutput>()
    private var current: InputOutput? = null

    override fun toString(): String {
        return "id: ${listener.id} name: ${listener.target.name} size: ${history.size}"
    }

    override fun equals(other: Any?): Boolean {
        return other is GameLog && listener == other.listener
    }

    fun addInput(input: String) {
        if (current != null) history.add(current!!)
        current = InputOutput(input)
    }

    fun endCurrent(){
        if (current != null) history.add(current!!)
        current = null
    }

    fun print(message: String) {
        if (current == null) current = InputOutput()
        current!!.outPut.add(message)
        if (GameState.properties.values.getBoolean(PRINT_WITHOUT_FLUSH)) println(message)
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

}