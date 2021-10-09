package core.history

import core.GameState
import system.debug.DebugType
import core.target.Target


/**
 * Only displayed to this target (you)
 */
fun Target.displayYou(message: String) {
    ChatHistoryManager.getHistory(this).print(message)
}

/**
 * Displayed to everyone but you (the calling target)
 */
fun Target.displayOthers(message: String) {
    ChatHistoryManager.getHistory(this).print(message)
}

/**
 * The message is evaluated for each listener
 */
//Maybe don't use this guy in favor of the one below with a source
fun display(message: (Target) -> String) {
    ChatHistoryManager.histories.forEach { history ->
        val messageText = message(history.listener)
        history.print(messageText)
    }
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

fun display(message: String) {
    ChatHistoryManager.histories.forEach { it.print(message) }
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

object ChatHistoryManager {
    //Target hashcode changes and breaks == as a key lookup
    //Instead use a list and filter for referential equality
    val histories = mutableListOf<ChatHistory>()

    init {
        track(GameState.player)
    }

    var first = histories.first()

    fun track(player: Target) {
        histories.add(ChatHistory(player))
    }

    fun reset() {
        histories.clear()
        track(GameState.player)
        first = histories.first()
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
        return candidate!!
//        histories.putIfAbsent(source, ChatHistory(source))
//        return histories[source]!!
    }

}
