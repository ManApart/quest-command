package interact.actions

import core.events.EventListener
import core.gameState.Activator
import core.gameState.Stat
import interact.UseEvent
import status.StatChangeEvent
import system.EventManager

class ChopWood : EventListener<UseEvent>() {
    val chopHealth = "chop-health"

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (event.target is Activator) {
            event.target.properties.tags.has("Wood") && event.target.creature != null && event.target.creature.soul.hasStat(chopHealth) && event.source.properties.values.getInt("chopDamage", 0) != 0
        } else {
            false
        }
    }

    override fun execute(event: UseEvent) {
        println("The ${event.source} hacks at ${event.target.name}.")
            val damageDone = -event.source.properties.values.getInt("chopDamage", 0)
            EventManager.postEvent(StatChangeEvent((event.target as Activator).creature!!, event.source.name, chopHealth, damageDone))
    }
}