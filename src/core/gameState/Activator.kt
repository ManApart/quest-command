package core.gameState

import core.events.Event

class Activator(override val name: String, val description: String = "", private val trigger: Trigger, private val triggeredEvent: TriggeredEvent, tags: List<String> = listOf()) : Target {
    override val tags = Tags(tags)


    fun evaluateAndExecute(event: Event){
        if (trigger.matches(event)){
            triggeredEvent.execute(event)
        }
    }

}