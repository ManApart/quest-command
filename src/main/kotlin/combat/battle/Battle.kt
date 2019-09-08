package combat.battle

import combat.Combatant
import core.gameState.Target
import core.gameState.GameState

import core.history.display
import system.EventManager

class Battle(combatantCreatures: List<Target>) {
    private val combatants = mutableListOf<Combatant>()

    init {
        combatantCreatures.forEach {
            combatants.add(Combatant(it))
        }
    }

    fun getPlayerCombatant() =
            getCombatant(GameState.player)!!

    fun getCombatant(creature: Target): Combatant? {
        return combatants.firstOrNull { it.target == creature }
    }

    fun getOpponent(creature: Target): Combatant? {
        return combatants.firstOrNull { it.target != creature }
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

    fun start() {
        GameState.player.canRest = false
        GameState.player.canTravel = false
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
                it.lastAttacked = it.action?.actionTarget ?: it.lastAttacked
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
        display(combatants.joinToString(" and ") { it.target.name } + " are ${getCombatantDistance()} away from each other.")
        combatants.forEach {
            println("\t${it.status()}")
        }
    }

    fun getCombatantDistance() : Int {
        return combatants.first().target.position.getDistance(combatants.last().target.position)
    }

}