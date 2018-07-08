package interact.actions

import interact.UseEvent

class ScratchSurface : Action {
    override fun matches(event: UseEvent): Boolean {
        return event.target.tags.has("Wood") && event.source.tags.has("Sharp") && event.source.properties.getInt("chopDamage", 0) == 0
    }

    override fun execute(event: UseEvent) {
        println("The ${event.source} scratches ${event.target.name} but does no discernible harm.")
    }
}