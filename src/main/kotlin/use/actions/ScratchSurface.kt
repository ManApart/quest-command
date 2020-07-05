package use.actions

import core.history.display
import use.UseEvent
import use.UseListener

class ScratchSurface : UseListener() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.target.properties.tags.has("Wood")
                && event.used.properties.tags.has("Sharp")
                && event.used.properties.values.getInt("chopDamage", 0) == 0
    }

    override fun executeUseEvent(event: UseEvent) {
        display("The ${event.used} scratches ${event.target.name} but does no discernible harm.")
    }
}