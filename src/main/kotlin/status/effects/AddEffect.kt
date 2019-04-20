package status.effects

import core.events.EventListener

class AddEffect: EventListener<AddEffectEvent>() {
    override fun shouldExecute(event: AddEffectEvent): Boolean {
        return true
    }
    override fun execute(event: AddEffectEvent) {
        event.target.soul.effects.add(event.effect)
    }
}