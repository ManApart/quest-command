package combat.battle

import combat.Combatent
import core.gameState.Creature
import core.gameState.GameState
import system.EventManager

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
           getCombatant(GameState.player.creature)!!

    fun getCombatant(creature: Creature) : Combatent? {
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
        println("The battle ends.")
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
                println("You should have been able to do something by now. Something is wrong.")
                lastFired = 0
            }
            else -> EventManager.postEvent(BattleTurnEvent())
        }
    }

}