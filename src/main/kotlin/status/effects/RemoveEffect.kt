package status.effects

import core.events.EventListener

class RemoveEffect: EventListener<RemoveEffectEvent>() {
    override fun shouldExecute(event: RemoveEffectEvent): Boolean {
        return true
    }

    override fun execute(event: RemoveEffectEvent) {
        event.target.soul.effects.remove(event.target.soul.getEffectOrNull(event.effect.name))
    }
}