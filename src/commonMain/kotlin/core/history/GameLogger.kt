package core.history

import core.GameState
import core.Player

object GameLogger {
    val histories = mutableMapOf<String, GameLog>()

    init {
        GameState.players.values.forEach {
            track(it)
        }
    }

    fun reset() {
        histories.clear()
        GameState.players.values.forEach { track(it) }
    }

    fun track(player: Player) {
        histories[player.name] = GameLog(player)
    }

    fun stopTracking(player: Player) {
        histories.remove(player.name)
    }

    fun addInput(input: String) {
        //Should we only add based on the current actor or add to all?
        histories.values.forEach { it.addInput(input) }
    }

    fun endCurrent() {
        histories.values.forEach { it.endCurrent() }
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
        var candidate = histories[source.name]
        if (candidate == null) {
            candidate = GameLog(source)
            histories[source.name] = candidate
        }
        return candidate
    }

}
