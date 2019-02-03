package combat.battle

import combat.Combatant
import combat.battle.position.TargetDistance
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Item
import core.history.display
import system.EventManager

class Battle(combatantCreatures: List<Creature>, private var targetDistance: TargetDistance = TargetDistance.BOW) {
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

    fun getDistance() : TargetDistance {
        return targetDistance
    }

//    fun isAttackInRange(weapon: Item) : Boolean {
//        return targetDistance == TargetDistance.getRangeOfItem(weapon)
//    }

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