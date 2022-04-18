package core.history

import core.GameState
import core.Player
import core.thing.Thing
import system.debug.DebugType


/**
 * Only displayed to this thing (you)
 */
fun Thing.displayToMe(message: String) {
    GameState.getPlayer(this)?.let {
        GameLogger.getHistory(it).print(message)
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
        GameLogger.histories.values.filter { it.listener.thing !== this }.forEach { it.print(message) }
    } else {
        GameLogger.histories.values.forEach { it.print(message) }
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
    GameLogger.histories.values.forEach { history ->
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
    GameLogger.histories.values
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
    val histories = mutableMapOf<String, GameLog>()

    init {
        GameState.players.values.forEach {
            track(it)
        }
    }

    fun track(player: Player) {
        histories[player.name] = GameLog(player)
    }

    fun stopTracking(player: Player) {
        histories.remove(player.name)
    }

    fun reset() {
        histories.clear()
        track(GameState.player)
    }

    fun addInput(input: String) {
        //Should we only add based on the current actor or add to all?
        histories.values.forEach { it.addInput(input) }
    }

    fun setTimeTaken(timeTaken: Long) {
        histories.values.forEach { it.getCurrent().timeTaken = timeTaken }
    }

    fun hasHistory(source: Player): Boolean {
        return histories.containsKey(source.name)
    }

    //Shortcut for terminal printing, tests, etc where we only care about the main history
    fun getMainHistory(): GameLog {
        return getHistory(GameState.player)
    }

    fun getHistory(source: Player): GameLog {
        var candidate = histories.get(source.name)
        if (candidate == null) {
            candidate = GameLog(source)
            histories[source.name] = candidate
        }
        return candidate
    }

}
