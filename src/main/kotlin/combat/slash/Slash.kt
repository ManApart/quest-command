package combat.slash

import combat.AttackManager
import combat.AttackType
import core.events.EventListener

class Slash : EventListener<SlashEvent>() {
    override fun execute(event: SlashEvent) {
        AttackManager.execute(AttackType.SLASH, event.source, event.sourcePart, event.target, event.position, event)
    }
}