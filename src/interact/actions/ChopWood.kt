package interact.actions

import core.gameState.Activator
import core.gameState.Stat
import interact.UseEvent
import status.StatChangeEvent
import system.EventManager

class ChopWood : Action {
    override fun matches(event: UseEvent): Boolean {
        return event.target.properties.tags.has("Wood") && event.source.properties.values.getInt("chopDamage", 0) != 0
    }

    override fun execute(event: UseEvent) {
        println("The ${event.source} hacks at ${event.target.name}.")
        if (event.target is Activator && event.target.soul.hasStat(Stat.HEALTH)){
            val damageDone = event.source.properties.values.getInt("chopDamage", 0)
            EventManager.postEvent(StatChangeEvent(event.target, event.source.name, Stat.HEALTH, damageDone))
        }
    }
}