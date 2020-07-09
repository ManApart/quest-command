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
                && event.target.properties.tags.has("Wood")
                && event.target.properties.values.has(DamageType.CHOP.health)
                && event.used.properties.values.getInt(DamageType.CHOP.damage, 0) != 0
    }

    override fun execute(event: UseEvent) {
        display("The ${event.used} hacks at ${event.target.name}.")
        val damageDone = -event.used.properties.values.getInt(DamageType.CHOP.damage, 0)
        EventManager.postEvent(PropertyStatChangeEvent(event.target, event.used.name, DamageType.CHOP.health, damageDone))
    }
}