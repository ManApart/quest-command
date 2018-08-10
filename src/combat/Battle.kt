package combat

import core.gameState.Creature
import core.gameState.GameState

class Battle(enemy: Creature) {
    var distance = 10
    val combatents = mutableListOf<Combatent>()

    init {
        combatents.add(Combatent(enemy))
        combatents.add(Combatent(GameState.player.creature))
    }
}