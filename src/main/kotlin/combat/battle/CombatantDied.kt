package combat.battle

import core.events.EventListener
import core.gameState.GameState
import status.CreatureDiedEvent

class CombatantDied : EventListener<CreatureDiedEvent>() {
    override fun execute(event: CreatureDiedEvent) {
        val battle = GameState.battle
        val combatant = battle?.getCombatant(event.creature)
        if (combatant != null){
            battle.removeCombatant(combatant)
        }
    }

    override fun getPriorityRank(): Int {
        return 1
    }
}