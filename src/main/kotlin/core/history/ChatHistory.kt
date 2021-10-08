package core.history

import core.GameState
import core.target.Target
import system.debug.DebugType

fun display(message: String) {
    ChatHistory.print("NotUsedYet", message)
}

fun displayUpdate(message: String, sleep: Long = 50) {
    if (GameState.getDebugBoolean(DebugType.DISPLAY_UPDATES)) {
        print("\r$message                                ")
        System.out.flush()
        Thread.sleep(sleep)
    }
}

fun displayUpdateEnd(message: String) {
    print("\r$message                                \n")
    System.out.flush()
}

fun displayIf(message: String, shouldDisplay: Boolean) {
    if (shouldDisplay) {
        display(message)
    }
}

object ChatHistory {
    val history = mutableListOf<InputOutput>()
    private var current = InputOutput()
    private val ignored = mutableListOf<String>()

    fun addInput(input: String) {
        history.add(current)
        current = InputOutput(input)
    }

    fun print(id: String, message: String) {
        current.outPut.add(message)
        if (!ignored.contains(id)) {
            println(message)
        }
    }

    fun ignoreMessage(id: String) {
        ignored.add(id)
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


/**
 * Only displayed to this target (you)
 */
fun Target.displayYou(message: String) {
    ChatHistoryManager.histories[this]?.print(message)
}

/**
 * The message is evaluated for each listener
 */
//Maybe don't use this guy in favor of the one below with a source
fun display(message: (Target) -> String) {
    ChatHistoryManager.histories.values.forEach { history ->
        val messageText = message(history.listener)
        history.print(messageText)
    }
}

/**
 * The message is evaluated for each listener that perceives this target
 */
fun Target.display(message: (Target) -> String) {
    ChatHistoryManager.histories.values
        .filter { it.listener.perceives(this)}
        .forEach { history ->
            val messageText = message(history.listener)
            history.print(messageText)
        }
}

object ChatHistoryManager {
    val histories = mutableMapOf<Target, ChatHistoryThing>()

    init {
        histories[GameState.player] = ChatHistoryThing(GameState.player)
    }
}