package interact.actions

import interact.UseEvent

class ChopWood : Action {
    override fun matches(event: UseEvent): Boolean {
        return event.target.properties.tags.has("Wood") && event.source.properties.values.getInt("chopDamage", 0) != 0
    }

    override fun execute(event: UseEvent) {
        val damageDone = event.source.properties.values.getInt("chopDamage", 0)
        println("The ${event.source} hacks at ${event.target.name}.")
    }
}