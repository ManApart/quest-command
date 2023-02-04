package core

import core.commands.CommandParsers
import core.events.Event
import core.history.GameLogger
import core.properties.Properties
import core.thing.Thing
import core.utility.lazyM
import system.debug.DebugType
import time.TimeManager

object GameState {
    var gameName = "Kanbara"
    var properties = Properties()
    val timeManager = TimeManager()
    //The default player is just a stub replaced by new game or load.
    val players = mutableMapOf("Player" to Player("Player", Thing("Player")) )
    var player = players.values.first()
    val aliases = mutableMapOf<String, String>()

    suspend fun reset() {
        players.clear()
        putPlayer(GameManager.newPlayer())
        player = players.values.first()
        properties = Properties()
    }

    fun getPlayer(name: String): Player? {
        return players[name.lowercase()]
    }

    fun getPlayer(creature: Thing): Player? {
        return players.values.firstOrNull { it.thing == creature }
    }

    fun putPlayer(player: Player, isMainPlayer: Boolean = false) {
        players[player.name.lowercase()] = player
        GameLogger.track(player)
        CommandParsers.addParser(player)
        if (isMainPlayer) GameState.player = player
    }

    fun getDebugBoolean(key: DebugType): Boolean {
        return properties.values.getBoolean(key.propertyName)
    }

    fun putDebug(key: DebugType, value: Boolean) {
        return properties.values.put(key.propertyName, value)
    }

    fun putDebug(key: DebugType, value: Int) {
        return properties.values.put(key.propertyName, value)
    }

}

fun eventWithPlayer(creature: Thing, event: (Player) -> Event): Event? {
    return GameState.getPlayer(creature)?.let { event(it) }
}

fun startupLog(message: String) {
    if (GameState.properties.values.getBoolean(VERBOSE_STARTUP)) println(message)
}

const val AUTO_SAVE = "autosave"
const val AUTO_LOAD = "autoload"
const val VERBOSE_STARTUP = "verbose startup"
const val TEST_SAVE_FOLDER = "use test save folder"
const val SKIP_SAVE_STATS = "skip save stats"
const val LAST_SAVE_GAME_NAME = "last save character name"
const val PRINT_WITHOUT_FLUSH = "print without needing to flush histories"