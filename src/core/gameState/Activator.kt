package core.gameState

import core.events.Event

class Activator(override val name: String, override val description: String = "", val soul: Soul = Soul(), private val triggers: List<Trigger> = listOf(), override val properties: Properties = Properties()) : Target {

    fun evaluateAndExecute(event: Event){
        triggers.forEach { it.evaluateAndExecute(event) }
    }

}

fun Target.consume(event: Event){
    if (this is Activator) {
        this.evaluateAndExecute(event)
    }
}
