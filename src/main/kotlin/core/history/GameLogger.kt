package core.history

import core.GameState
import core.Player
import core.thing.Thing
import system.debug.DebugType


/**
 * Only displayed to this thing (you)
 */
fun Thing.displayToMe(message: String) {
    if (isPlayer()) {
        GameLogger.getHistory(GameState.getPlayer(this)).print(message)
    }
}

fun Player.displayToMe(message: String) {
    GameLogger.getHistory(this).print(message)
}

/**
 * Displayed to everyone but you (the calling thing)
 */
fun Thing.displayToOthers(message: String) {
    if (isPlayer()) {
        GameLogger.histories.filter { it.listener.thing !== this }.forEach { it.print(message) }
    } else {
        GameLogger.histories.forEach { it.print(message) }
    }
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
fun display(message: (Player) -> String) {
    GameLogger.histories.forEach { history ->
        val messageText = message(history.listener)
        history.print(messageText)
    }
}

/**
 * The message is evaluated for each listener that perceives this thing
 */
fun Thing.display(message: String) {
    this.display { message }
}

fun Player.display(message: String) {
    this.display { message }
}

fun Player.display(message: (Player) -> String) {
    this.thing.display(message)
}

/**
 * The message is evaluated for each listener that perceives this thing
 */
fun Thing.display(message: (Player) -> String) {
    GameLogger.histories
        .filter { it.listener.thing.perceives(this) }
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

object GameLogger {
    //Thing hashcode changes and breaks == as a key lookup
    //Instead use a list and filter for referential equality
    val histories = mutableSetOf<GameLog>()

    init {
        track(GameState.player)
    }

    var main = getHistory(GameState.player)

    fun track(player: Player) {
        histories.add(GameLog(player))
    }

    fun stopTracking(player: Player) {
        histories.removeIf {it.listener.id == player.id}
    }

    fun trackNewMain(player: Player) {
        track(player)
        main = getHistory(GameState.player)
    }

    fun reset() {
        histories.clear()
        trackNewMain(GameState.player)
    }

    fun addInput(input: String) {
        //Should we only add based on the current actor or add to all?
        histories.forEach { it.addInput(input) }
    }

    fun setTimeTaken(timeTaken: Long) {
        histories.forEach { it.getCurrent().timeTaken = timeTaken }
    }

    fun hasHistory(source: Player): Boolean {
        return histories.any { it.listener === source }
    }

    fun getHistory(source: Player): GameLog {
        var candidate = histories.firstOrNull { it.listener == source }
        if (candidate == null) {
            candidate = GameLog(source)
            histories.add(candidate)
        }
        return candidate
    }

}
