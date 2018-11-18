package combat.battle

import combat.Combatant
import core.gameState.Creature
import core.gameState.GameState
import core.history.display
import system.EventManager

class Battle(combatantCreatures: List<Creature>) {
    var distance = 10
    private var lastFired = 0
    val combatants = mutableListOf<Combatant>()
    init {
        combatantCreatures.forEach {
            combatants.add(Combatant(it))
        }
    }
    var playerLastAttacked = getPlayerCombatant().creature


    private fun getPlayerCombatant() =
           getCombatant(GameState.player.creature)!!

    fun getCombatant(creature: Creature) : Combatant? {
        return combatants.firstOrNull { it.creature == creature }
    }

    fun takeTurn(){
        if (isOver()){
            clearBattle()
        } else {
            executeTurn()
        }
    }

    private fun isOver() : Boolean {
        return combatants.size <= 1
    }

    private fun clearBattle() {
        GameState.battle = null
        GameState.player.canTravel = true
        GameState.player.canRest = true
        display("The battle ends.")
        EventManager.postEvent(BattleEndedEvent())
    }

    private fun executeTurn() {
        lastFired ++
        var playerTurn = false

        combatants.forEach { it.increaseActionPoints() }
        combatants.forEach {
            if (it.canAct()){
                if (it.isPlayer()) {
                    playerTurn = true
                }
                it.act()
            }
        }

        when {
            playerTurn -> lastFired = 0
            lastFired > 100 -> {
                display("You should have been able to do something by now. Something is wrong.")
                lastFired = 0
            }
            else -> EventManager.postEvent(BattleTurnEvent())
        }
    }

}