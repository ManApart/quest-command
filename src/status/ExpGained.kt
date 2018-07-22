package status

import core.events.EventListener
import core.gameState.GameState

class ExpGained : EventListener<ExpGainedEvent>() {
    override fun shouldExecute(event: ExpGainedEvent): Boolean {
        return event.creature == GameState.player.creature
    }

    override fun execute(event: ExpGainedEvent) {
        val stat = event.creature.soul.getStatOrNull(event.stat)
        if (stat != null) {
            if (stat.expExponential > 1) {
                println("You gained ${event.amount} exp in ${stat.name}")
            }
            stat.addEXP(event.amount, event.creature)
        }
    }
}