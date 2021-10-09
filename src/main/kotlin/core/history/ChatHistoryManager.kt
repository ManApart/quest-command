package core.history

import core.GameState
import core.target.Target
import system.debug.DebugType


/**
 * Only displayed to this target (you)
 */
//TODO - rename displayToMe
fun Target.displayYou(message: String) {
    ChatHistoryManager.getHistory(this).print(message)
}

/**
 * Displayed to everyone but you (the calling target)
 */
//TODO - rename displayToOthers
fun Target.displayOthers(message: String) {
    ChatHistoryManager.getHistory(this).print(message)
}

/**
 * The message is evaluated for each listener, regardless of perception
 */
fun display(message: String) {
    display { message }
}

/**
 * The message is evaluated for each listener, regardless of perception
 */
fun display(message: (Target) -> String) {
    ChatHistoryManager.histories.forEach { history ->
        val messageText = message(history.listener)
        history.print(messageText)
    }
}

/**
 * The message is evaluated for each listener that perceives this target
 */
fun Target.display(message: String) {
    this.display { message }
}

/**
 * The message is evaluated for each listener that perceives this target
 */
fun Target.display(message: (Target) -> String) {
    ChatHistoryManager.histories
        .filter { it.listener.perceives(this) }
        .forEach { history ->
            val messageText = message(history.listener)
            history.print(messageText)
        }
}

//TODO - how does this work with chat history? Is this valueable?
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

object ChatHistoryManager {
    //Target hashcode changes and breaks == as a key lookup
    //Instead use a list and filter for referential equality
    val histories = mutableListOf<ChatHistory>()

    init {
        track(GameState.player)
    }

    var main = getHistory(GameState.player)

    fun track(player: Target) {
        histories.add(ChatHistory(player))
    }

    fun reset() {
        histories.clear()
        track(GameState.player)
        main = getHistory(GameState.player)
    }

    fun addInput(input: String) {
        //Should we only add based on the current actor or add to all?
        histories.forEach { it.addInput(input) }
    }

    fun setTimeTaken(timeTaken: Long) {
        histories.forEach { it.getCurrent().timeTaken = timeTaken }
    }

    fun getHistory(source: Target): ChatHistory {
        var candidate = histories.firstOrNull { it.listener === source }
        if (candidate == null) {
            candidate = ChatHistory(source)
            histories.add(candidate)
        }
        return candidate
    }

    fun flushHistories() {
        histories.forEach { it.flush() }
    }

}
