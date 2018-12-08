package core.gameState

import combat.battle.Battle

object GameState {
    var player = Player()
    var battle: Battle? = null


    fun reset() {
        player = Player()
        battle = null
    }

}