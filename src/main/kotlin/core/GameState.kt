package core

import combat.battle.Battle
import core.properties.Properties
import time.gameTick.TimeManager

object GameState {
    var gameName = "Kanbara"
    var player = GameManager.newPlayer()
    var battle: Battle? = null
    var properties = Properties()
    val timeManager = TimeManager()


    fun reset() {
        player = GameManager.newPlayer()
        battle = null
        properties = Properties()
    }

}


const val AUTO_SAVE = "autosave"
const val AUTO_LOAD = "autoload"
const val SKIP_SAVE_STATS = "skip save stats"
const val LAST_SAVE_CHARACTER_NAME = "last save character name"
const val LAST_SAVE_GAME_NAME = "last save character name"

