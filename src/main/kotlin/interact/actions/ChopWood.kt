package interact.actions

import core.events.EventListener
import core.gameState.Activator
import core.gameState.GameState
import core.history.display
import interact.UseEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class ChopWood : EventListener<UseEvent>() {
    private val chopHealth = "chopHealth"

    override fun shouldExecute(event: UseEvent): Boolean {
        return GameState.player.canInteract
                && event.target is Activator
                && event.target.properties.tags.has("Wood")
                && event.target.creature.soul.hasStat(chopHealth)
                && event.used.properties.values.getInt("chopDamage", 0) != 0
    }

    override fun execute(event: UseEvent) {
        display("The ${event.used} hacks at ${event.target.name}.")
        val damageDone = -event.used.properties.values.getInt("chopDamage", 0)
        EventManager.postEvent(StatChangeEvent((event.target as Activator).creature, event.used.name, chopHealth, damageDone))
    }
}