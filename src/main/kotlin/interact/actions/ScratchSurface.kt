package interact.actions

import core.events.EventListener
import core.history.display
import interact.UseEvent

class ScratchSurface : EventListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.target.properties.tags.has("Wood") && event.source.properties.tags.has("Sharp") && event.source.properties.values.getInt("chopDamage", 0) == 0
    }

    override fun execute(event: UseEvent) {
        display("The ${event.source} scratches ${event.target.name} but does no discernible harm.")
    }
}