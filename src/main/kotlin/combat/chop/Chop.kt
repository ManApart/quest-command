package combat.chop

import combat.AttackManager
import combat.AttackType
import core.events.EventListener

class Chop : EventListener<ChopEvent>() {
    override fun execute(event: ChopEvent) {
        AttackManager.execute(AttackType.CHOP, event.source, event.sourcePart, event.target, event.position, event)
    }
}