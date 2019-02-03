package combat.battle

import combat.Combatant
import combat.battle.position.TargetDistance
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.isPlayer
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

    fun getCombatant(creature: Creature): Combatant? {
        return combatants.firstOrNull { it.creature == creature }
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

        combatants.forEach { it.tick() }
        combatants.forEach {
            if (it.isActionReady()) {
                EventManager.postEvent(it.action!!.getActionEvent())
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
            else -> EventManager.postEvent(BattleTurnEvent())
        }
    }

    fun addAction(source: Creature, action: BattleAction) {
        val combatant = getOrAddCombatant(source)
        combatant.action = action
    }

}