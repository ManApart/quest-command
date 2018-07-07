package core.gameState

import core.events.Event

class Activator(override val name: String, override val description: String = "", private val triggers: List<Trigger> = listOf(), tags: List<String> = listOf()) : Target {
    override val tags = Tags(tags)

    fun evaluateAndExecute(event: Event){
        triggers.forEach { it.evaluateAndExecute(event) }
    }

}

fun Target.consume(event: Event){
    if (this is Activator) {
        this.evaluateAndExecute(event)
    }
}
