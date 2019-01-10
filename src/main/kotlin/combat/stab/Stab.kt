package combat.stab

import combat.AttackManager
import combat.AttackType
import core.events.EventListener

class Stab : EventListener<StabEvent>() {
    override fun execute(event: StabEvent) {
        AttackManager.execute(AttackType.STAB, event.source, event.sourcePart, event.target, event.position, event)
    }
}