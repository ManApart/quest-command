package combat.battle

import combat.Combatant
import combat.battle.position.TargetDistance
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.isPlayer
import core.gameState.stat.HEALTH
import core.history.display
import system.EventManager

class Battle(combatantCreatures: List<Creature>, private var targetDistance: TargetDistance = TargetDistance.BOW) {
    private var lastFired = 0
    private val combatants = mutableListOf<Combatant>()

    init {
        combatantCreatures.forEach {
            combatants.add(Combatant(it))
        }
    }

    var playerLastAttacked = getPlayerCombatant().creature

    private fun getPlayerCombatant() =
            getCombatant(GameState.player.creature)!!

    fun getCombatant(creature: Creature): Combatant? {
        return combatants.firstOrNull { it.creature == creature }
    }

    fun removeCombatant(combatant: Combatant) {
        combatants.remove(combatant)
    }

    private fun getOrAddCombatant(creature: Creature) : Combatant {
        if (getCombatant(creature) == null){
            combatants.add(Combatant(creature))
        }
        return getCombatant(creature)!!
    }

    fun getDistance(): TargetDistance {
        return targetDistance
    }

    fun takeTurn() {
//        removeDeadCombatants()
        if (isOver()) {
            clearBattle()
        } else {
            executeTurn()
        }
    }

    private fun isOver(): Boolean {
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
        lastFired++
        var playerTurn = false
        var actionFired = false

        combatants.forEach { it.tick() }
        combatants.forEach {
            if (it.isActionReady()) {
                actionFired = true
                inspectBattle()
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
            } else if (it.canChooseAction()) {
                if (it.creature.isPlayer()) {
                    playerTurn = true
                }
                it.chooseAction()
            }
        }

        when {
            playerTurn -> lastFired = 0
            lastFired > 100 -> {
                display("You should have been able to do something by now. Something is wrong.")
                lastFired = 0
            }
            !actionFired -> EventManager.postEvent(BattleTurnEvent())
        }
    }

    private fun removeDeadCombatants() {
        combatants.removeAll { it.creature.soul.getCurrent(HEALTH) == 0 }
    }

    fun addAction(source: Creature, action: BattleAction) {
        val combatant = getOrAddCombatant(source)
        combatant.action = action
        EventManager.postEvent(BattleTurnEvent())
    }

    private fun inspectBattle() {
        println("Battle:")
        combatants.forEach {
            println("\t${it.status()}")
        }
    }

}