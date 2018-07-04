package processing.use

import events.UseItemEvent

interface Use {
    fun matches(event: UseItemEvent) : Boolean
    fun execute(event: UseItemEvent)
}