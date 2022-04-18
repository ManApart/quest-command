package core

import core.events.Event
import core.properties.Properties
import core.thing.Thing
import system.debug.DebugType
import time.TimeManager

object GameState {
    var gameName = "Kanbara"
    var properties = Properties()
    val timeManager = TimeManager()
    val players = mutableMapOf("Player" to GameManager.newPlayer())
    val player get() = players.values.first()
    val aliases = mutableMapOf<String, String>()

    fun reset() {
        players.clear()
        putPlayer(GameManager.newPlayer())
        properties = Properties()
    }

    fun getPlayer(name: String): Player? {
        return players[name.lowercase()]
    }

    fun getPlayer(creature: Thing): Player? {
        return players.values.firstOrNull { it.thing == creature }
    }

    fun putPlayer(player: Player){
        players[player.name.lowercase()] = player
    }

    fun getDebugBoolean(key: DebugType): Boolean {
        return properties.values.getBoolean(key.propertyName)
    }

    fun putDebug(key: DebugType, value: Boolean){
        return properties.values.put(key.propertyName, value)
    }

    fun putDebug(key: DebugType, value: Int){
        return properties.values.put(key.propertyName, value)
    }

}

fun eventWithPlayer(creature: Thing, event: (Player) -> Event): Event? {
    return GameState.getPlayer(creature)?.let { event(it) }
}


const val AUTO_SAVE = "autosave"
const val AUTO_LOAD = "autoload"
const val SKIP_SAVE_STATS = "skip save stats"
const val LAST_SAVE_GAME_NAME = "last save character name"
const val PRINT_WITHOUT_FLUSH = "print without needing to flush histories"
const val POLL_CONNECTION = "poll server on cadence for updates"