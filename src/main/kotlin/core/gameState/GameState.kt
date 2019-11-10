package core.gameState

import combat.battle.Battle

object GameState {
    var player = Player()
    var battle: Battle? = null
    val properties = Properties()


    fun reset() {
        player = Player()
        battle = null
    }

}


const val AUTO_SAVE = "autosave"

