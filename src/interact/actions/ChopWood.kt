package interact.actions

import interact.UseEvent

class ChopWood : Action {
    override fun matches(event: UseEvent): Boolean {
        return event.target.tags.has("Wood") && event.source.properties.getInt("chopDamage", 0) != 0
    }

    override fun execute(event: UseEvent) {
        val damageDone = event.source.properties.getInt("chopDamage", 0)
        println("The ${event.source} hacks at ${event.target.name}.")
    }
}