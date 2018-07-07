package use.actions

import use.UseItemEvent

interface Action {
    fun matches(event: UseItemEvent) : Boolean
    fun execute(event: UseItemEvent)
}