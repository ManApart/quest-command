package use.actions

import core.history.display
import use.UseEvent
import use.UseListener

class ScratchSurface : UseListener() {
    override suspend fun shouldExecute(event: UseEvent): Boolean {
        return event.usedOn.properties.tags.has("Wood")
                && event.used.properties.tags.has("Sharp")
                && event.used.properties.values.getInt("chopDamage", 0) == 0
    }

    override suspend fun executeUseEvent(event: UseEvent) {
        event.creature.display("The ${event.used.name} scratches ${event.usedOn.name} but does no discernible harm.")
    }
}