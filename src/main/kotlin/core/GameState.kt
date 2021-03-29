package core

import conversation.Conversation
import core.properties.Properties
import time.TimeManager
import traveling.location.location.Location

object GameState {
    var gameName = "Kanbara"
    var properties = Properties()
    val timeManager = TimeManager()
    var player = GameManager.newPlayer()
    val aliases = mutableMapOf<String, String>()
    var conversation = Conversation(player, player)

    fun reset() {
        player = GameManager.newPlayer()
        properties = Properties()
    }

    fun currentLocation() : Location {
        return player.location.getLocation()
    }

}


const val AUTO_SAVE = "autosave"
const val AUTO_LOAD = "autoload"
const val SKIP_SAVE_STATS = "skip save stats"
const val LAST_SAVE_CHARACTER_NAME = "last save character name"
const val LAST_SAVE_GAME_NAME = "last save character name"