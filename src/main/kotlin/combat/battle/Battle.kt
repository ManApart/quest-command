package combat.battle

import combat.Combatant
import combat.battle.position.TargetDistance
import core.gameState.Target
import core.gameState.GameState

import core.gameState.stat.HEALTH
import core.history.display
import system.EventManager

class Battle(combatantCreatures: List<Target>, var targetDistance: TargetDistance = TargetDistance.BOW) {
    private val combatants = mutableListOf<Combatant>()

    init {
        combatantCreatures.forEach {
            combatants.add(Combatant(it))
        }
    }

    var playerLastAttacked = getPlayerCombatant()

    private fun getPlayerCombatant() =
            getCombatant(GameState.player)!!

    fun getCombatant(creature: Target): Combatant? {
        return combatants.firstOrNull { it.creature == creature }
    }

    fun removeCombatant(combatant: Combatant) {
        combatants.remove(combatant)
    }

    private fun getOrAddCombatant(creature: Target): Combatant {
        if (getCombatant(creature) == null) {
            combatants.add(Combatant(creature))
        }
        return getCombatant(creature)!!
    }

    fun takeTurn() {
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
        var takeAnotherTurn = true
        combatants.forEach { it.tick() }
        combatants.forEach {
            if (it.isActionReady()) {
                takeAnotherTurn = false
                EventManager.postEvent(it.action!!.getActionEvent())
                it.action = null
            } else if (it.canChooseAction()) {
                it.resetStance()
                it.chooseAction()
                takeAnotherTurn = false
            }
        }
        if (takeAnotherTurn) {
            EventManager.postEvent(BattleTurnEvent())
        }
    }

    fun addAction(source: Target, action: BattleAction) {
        val combatant = getOrAddCombatant(source)
        combatant.action = action
        EventManager.postEvent(BattleTurnEvent())
    }

    fun describe() {
        display(combatants.joinToString(" and ") { it.creature.name } + " are ${targetDistance.name} length from each other.")
        combatants.forEach {
            println("\t${it.status()}")
        }

    }

}