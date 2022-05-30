package use.actions

import core.history.display
import use.UseEvent
import use.UseListener

class ScratchSurface : UseListener() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.usedOn.properties.tags.has("Wood")
                && event.used.properties.tags.has("Sharp")
                && event.used.properties.values.getInt("chopDamage", 0) == 0
    }

    override fun executeUseEvent(event: UseEvent) {
        event.source.display("The ${event.used.name} scratches ${event.usedOn.name} but does no discernible harm.")
    }
}