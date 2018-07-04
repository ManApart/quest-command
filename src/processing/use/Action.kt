package processing.use

import events.UseItemEvent

interface Action {
    fun matches(event: UseItemEvent) : Boolean
    fun execute(event: UseItemEvent)
}