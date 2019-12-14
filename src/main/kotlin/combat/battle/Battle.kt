package combat.battle

import combat.Combatant
import core.commands.CommandParser
import core.GameState
import core.target.Target
import core.history.display
import core.history.displayUpdate
import core.history.displayUpdateEnd
import core.events.EventManager

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
        printUpdatingStatus()
        combatants.forEach { it.tick() }
        combatants.forEach {
            if (it.isActionReady()) {
                printUpdatingStatusEnd()
//                printTurnStatus()
                takeAnotherTurn = false
                EventManager.postEvent(it.action!!.getActionEvent())
                it.lastAttacked = it.action?.target?.target ?: it.lastAttacked
                it.action = null
            } else if (it.canChooseAction()) {
                printUpdatingStatusEnd()
//                printTurnStatus()
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
        if (!CommandParser.isPlayersTurn()) {
            display("It is ${CommandParser.commandSource}'s turn.")
        }
        combatants.forEach {
            println("\t${it.status()}")
        }
        printTurnStatus()
    }

    fun getCombatantDistance(): Int {
        return combatants.first().target.position.getDistance(combatants.last().target.position)
    }

    private fun printTurnStatus() {
        val combatantString = combatants.map {
            val target = it.target
            when {
                it.isActionReady() -> ""
                it.getActionPoints() == 0 -> ""
                it.canChooseAction() -> "$target is making a choice"
                it.action != null -> "$target is preforming an action with ${it.action!!.timeLeft} time left"
                else -> "$target is getting ready to make a choice: ${it.getActionPoints()}/100"
            }
        }.filter { it.isNotBlank() }.joinToString("\n")

        if (combatantString.isNotBlank()) {
            display(combatantString)
        }
    }

    private fun printUpdatingStatus() {
        val combatantString = combatants.joinToString("\t\t") {
            val points = it.getActionPoints()
            val timeLeft = it.action?.timeLeft ?: 0
            "${it.target.getDisplayName()}: $points AP, $timeLeft action time left"
        }
        displayUpdate(combatantString)
    }

    private fun printUpdatingStatusEnd() {
        val combatantString = combatants.joinToString("\t\t") {
            val points = it.getActionPoints()
            val timeLeft = it.action?.timeLeft ?: 0
            "${it.target.getDisplayName()}: $points AP, $timeLeft action time left"
        }
        displayUpdateEnd(combatantString)
    }

}