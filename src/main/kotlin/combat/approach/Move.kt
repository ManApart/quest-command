package combat.approach

import combat.battle.Distances
import core.events.EventListener
import core.gameState.stat.STAMINA
import core.history.display
import status.statChanged.StatChangeEvent
import system.EventManager
import kotlin.math.min


class Move : EventListener<MoveEvent>() {

    override fun execute(event: MoveEvent) {
        val desiredDistance = event.source.position.getDistance(event.target)
        val amount = getAmount(event, desiredDistance)
        if (amount == desiredDistance) {
            event.source.position = event.target
            display("${event.source} moved to ${event.target}.")
        } else {
            val newPos = event.source.position.getVectorInDirection(event.target, amount)
            event.source.position = newPos
            display("${event.source} moved $amount towards ${event.target} and is now at ${event.source.position}.")
        }
        if (event.useStamina) {
            EventManager.postEvent(StatChangeEvent(event.source, "moving", STAMINA, -amount / Distances.HUMAN_LENGTH, true))
        }
    }

    private fun getAmount(event: MoveEvent, desiredDistance: Int): Int {
        val abilityToMove = event.source.soul.getCurrent(STAMINA) * Distances.HUMAN_LENGTH
        return if (event.useStamina) {
            min(abilityToMove, desiredDistance)
        } else {
            desiredDistance
        }
    }
}


