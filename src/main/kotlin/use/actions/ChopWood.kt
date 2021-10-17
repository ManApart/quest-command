package use.actions

import combat.DamageType
import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.properties.propValChanged.PropertyStatChangeEvent
import use.UseEvent

class ChopWood : EventListener<UseEvent>() {

    override fun shouldExecute(event: UseEvent): Boolean {
        return event.source.canInteract()
                && event.thing.properties.tags.has("Wood")
                && event.thing.properties.values.has(DamageType.CHOP.health)
                && event.used.properties.values.getInt(DamageType.CHOP.damage, 0) != 0
    }

    override fun execute(event: UseEvent) {
        event.source.display("The ${event.used.name} hacks at ${event.thing.name}.")
        val damageDone = -event.used.properties.values.getInt(DamageType.CHOP.damage, 0)
        EventManager.postEvent(PropertyStatChangeEvent(event.thing, event.used.name, DamageType.CHOP.health, damageDone))
    }
}