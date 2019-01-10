package combat.crush

import combat.AttackManager
import combat.AttackType
import core.events.EventListener

class Crush : EventListener<CrushEvent>() {
    override fun execute(event: CrushEvent) {
        AttackManager.execute(AttackType.CRUSH, event.source, event.sourcePart, event.target, event.position, event)
    }
}