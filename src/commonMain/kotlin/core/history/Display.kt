package core.history

import core.GameState
import core.Player
import core.thing.Thing
import system.debug.DebugType


/**
 * Only displayed to this thing (you)
 */
fun Player.displayToMe(message: String) = GameLogger.getHistory(this).print(message)
fun Thing.displayToMe(message: String) {
    GameState.getPlayer(this)?.let {
        GameLogger.getHistory(it).print(message)
    }
}

/**
 * Displayed to everyone but you (the calling thing)
 */
fun Thing.displayToOthersGlobal(message: String) {
    if (isPlayer()) {
        GameLogger.histories.values.filter { it.listener.thing !== this }.forEach { it.print(message) }
    } else {
        GameLogger.histories.values.forEach { it.print(message) }
    }
}

/**
 * The message is evaluated for each listener, regardless of perception
 */
fun displayGlobal(message: String) = displayGlobal { message }
fun displayGlobal(message: (Player) -> String) {
    GameLogger.histories.values.forEach { history ->
        val messageText = message(history.listener)
        history.print(messageText)
    }
}

/**
 * The message is evaluated for each listener that perceives this thing
 */
suspend fun Player.display(message: String) = this.display { message }
suspend fun Player.display(message: (Player) -> String) = this.thing.display(message)
suspend fun Thing.display(message: String) = this.display { message }

suspend fun Thing.display(message: (Player) -> String) {
    GameLogger.histories.values
        .filter { it.listener.thing.perceives(this) }
        .forEach { history ->
            val messageText = message(history.listener)
            history.print(messageText)
        }
}

/**
 * The message is evaluated for each listener that perceives this thing, but excludes this thing itself
 */
suspend fun Player.displayToOthers(message: String) = this.thing.displayToOthers(message)
suspend fun Player.displayToOthers(message: (Player) -> String) = this.thing.displayToOthers(message)
suspend fun Thing.displayToOthers(message: String) = this.displayToOthers { message }

suspend fun Thing.displayToOthers(message: (Player) -> String) {
    GameLogger.histories.values
        .filter { it.listener.thing !== this && it.listener.thing.perceives(this) }
        .forEach { history ->
            val messageText = message(history.listener)
            history.print(messageText)
        }
}
