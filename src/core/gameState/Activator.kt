package core.gameState

import core.events.Event
import core.gameState.climb.Climbable

class Activator(override val name: String, override val description: String = "", override val soul: Soul = Soul(), override val inventory: Inventory = Inventory(), val climb: Climbable?, private val triggers: List<Trigger> = listOf(), override val properties: Properties = Properties()) : Creature {

    init {
        soul.addStats(properties.stats)
    }

    fun evaluateAndExecute(event: Event){
        triggers.forEach { it.evaluateAndExecute(event) }
    }

}

fun Target.consume(event: Event){
    if (this is Activator) {
        this.evaluateAndExecute(event)
    }
}
