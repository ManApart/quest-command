package interact.actions

import interact.UseEvent

class ScratchSurface : Action {
    override fun matches(event: UseEvent): Boolean {
        return event.target.properties.tags.has("Wood") && event.source.properties.tags.has("Sharp") && event.source.properties.values.getInt("chopDamage", 0) == 0
    }

    override fun execute(event: UseEvent) {
        println("The ${event.source} scratches ${event.target.name} but does no discernible harm.")
    }
}