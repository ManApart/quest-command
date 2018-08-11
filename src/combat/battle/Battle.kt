package combat.battle

import combat.Combatent
import core.gameState.Creature
import core.gameState.GameState

class Battle(combatantCreatures: List<Creature>) {
    var distance = 10
    var lastFired = 0
    val combatants = mutableListOf<Combatent>()
    init {
        combatantCreatures.forEach {
            combatants.add(Combatent(it))
        }
    }
    var playerLastAttacked = getPlayerCombatant().creature


    private fun getPlayerCombatant() =
            combatants.first { it.creature != GameState.player.creature }

    fun takeTurn(){
        lastFired ++
        combatants.forEach { it.increaseActionPoints() }
        combatants.forEach {
            if (it.canAct()){
                it.act()
            }
        }

        when {
            getPlayerCombatant().canAct() -> lastFired = 0
            lastFired > 100 -> {
                println("You should have been able to do something by now. Something is wrong.")
                lastFired = 0
            }
            else -> takeTurn()
        }


    }

}