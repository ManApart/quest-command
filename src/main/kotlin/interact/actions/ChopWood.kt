package interact.actions

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class ChopWood : EventListener<UseEvent>() {
    val chopHealth = "chopHealth"

    override fun shouldExecute(event: UseEvent): Boolean {
        return if (GameState.player.canInteract && event.target is Activator) {
            event.target.properties.tags.has("Wood") && event.target.creature.soul.hasStat(chopHealth) && event.source.properties.values.getInt("chopDamage", 0) != 0
        } else {
            false
        }
    }

    override fun execute(event: UseEvent) {
        println("The ${event.source} hacks at ${event.target.name}.")
            val damageDone = -event.source.properties.values.getInt("chopDamage", 0)
            EventManager.postEvent(StatChangeEvent((event.target as Activator).creature, event.source.name, chopHealth, damageDone))
    }
}