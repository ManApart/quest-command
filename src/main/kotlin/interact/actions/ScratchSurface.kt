package interact.actions

import core.history.display
import interact.UseEvent
import interact.UseListener

class ScratchSurface : UseListener<UseEvent>() {
    override fun shouldExecute(event: UseEvent): Boolean {
        return event.target.properties.tags.has("Wood")
                && event.used.properties.tags.has("Sharp")
                && event.used.properties.values.getInt("chopDamage", 0) == 0
    }

    override fun executeUseEvent(event: UseEvent) {
        display("The ${event.used} scratches ${event.target.name} but does no discernible harm.")
    }
}