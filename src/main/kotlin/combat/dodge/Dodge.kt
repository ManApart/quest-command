package combat.dodge

import core.events.EventListener
import core.gameState.GameState

class Dodge : EventListener<DodgeEvent>() {

    override fun execute(event: DodgeEvent) {
        if (GameState.battle != null) {
            val combatant = GameState.battle!!.getCombatant(event.source)
            if (combatant != null) {
                combatant.target.position = combatant.target.position + event.direction
//                display("${event.source} dodged to the ${event.direction}.")
            }
        }
    }

}