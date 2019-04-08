package combat.block

import core.events.EventListener
import core.gameState.GameState

class Block : EventListener<BlockEvent>() {

    override fun execute(event: BlockEvent) {
        if (GameState.battle != null) {
            val combatant = GameState.battle!!.getCombatant(event.source)
            if (combatant != null) {
                combatant.blockBodyPart = event.part
                combatant.blockPosition = event.direction.position
//                display("${event.source} Blockd to the ${event.direction}.")
            }
        }
    }

}