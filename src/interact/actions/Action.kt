package interact.actions

import interact.UseItemEvent

interface Action {
    fun matches(event: UseItemEvent) : Boolean
    fun execute(event: UseItemEvent)
}