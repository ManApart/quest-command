package core.history

import core.target.Target

class ChatHistoryThing(val listener: Target) {
    val history = mutableListOf<InputOutput>()
    private var current = InputOutput()

    fun addInput(input: String) {
        history.add(current)
        current = InputOutput(input)
    }

    fun print(message: String) {
        current.outPut.add(message)
        println(message)
    }

    fun reset() {
        history.clear()
        current = InputOutput()
    }

    fun getLastInput(): String {
        return current.input
    }

    fun getLastOutput(): String {
        return current.outPut.lastOrNull() ?: ""
    }

    fun getLastOutputs(): List<String> {
        return current.outPut
    }

    fun getCurrent(): InputOutput {
        return current
    }
}