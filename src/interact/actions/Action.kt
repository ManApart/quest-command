package interact.actions

import interact.UseEvent

interface Action {
    fun matches(event: UseEvent) : Boolean
    fun execute(event: UseEvent)
}