package combat.dodge

import core.events.EventListener
import core.gameState.GameState
import core.history.display

class Dodge : EventListener<DodgeEvent>() {

    override fun execute(event: DodgeEvent) {
        if (GameState.battle != null) {
            val combatant = GameState.battle!!.getCombatant(event.source)
            if (combatant != null) {
                combatant.position = event.direction.position
//                display("${event.source} dodged to the ${event.direction}.")
            }
        }
    }

}