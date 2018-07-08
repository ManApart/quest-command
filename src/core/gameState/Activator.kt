package core.gameState

import core.events.Event

class Activator(override val name: String, override val description: String = "", private val triggers: List<Trigger> = listOf(), tags: List<String> = listOf(),  properties: Map<String, String> = HashMap()) : Target {
    override val tags = Tags(tags)
    override val properties: Properties = Properties(properties)

    fun evaluateAndExecute(event: Event){
        triggers.forEach { it.evaluateAndExecute(event) }
    }

}

fun Target.consume(event: Event){
    if (this is Activator) {
        this.evaluateAndExecute(event)
    }
}
