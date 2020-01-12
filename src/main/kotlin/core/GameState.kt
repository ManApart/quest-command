package core

import combat.battle.Battle
import core.properties.Properties

object GameState {
    var gameName = "Kanbara"
    var player = GameManager.newPlayer()
    var battle: Battle? = null
    var properties = Properties()


    fun reset() {
        player = GameManager.newPlayer()
        battle = null
        properties = Properties()
    }

}


const val AUTO_SAVE = "autosave"
const val SKIP_SAVE_STATS = "skip save stats"
const val LAST_SAVE_CHARACTER_NAME = "last save character name"

