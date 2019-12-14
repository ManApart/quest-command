package core

import combat.battle.Battle
import core.properties.Properties
import core.target.Player

object GameState {
    var player = Player()
    var battle: Battle? = null
    var properties = Properties()


    fun reset() {
        player = Player()
        battle = null
        properties = Properties()
    }

}


const val AUTO_SAVE = "autosave"

