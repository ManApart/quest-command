package use

import core.events.EventListener
import use.actions.Action
import core.utility.ReflectionTools

object ActionManager {
    private val actions = loadActions()

    private fun loadActions(): List<Action> {
        return ReflectionTools.getAllUses().map { it.newInstance() }.toList()
    }

    class UseItemHandler() : EventListener<UseItemEvent>() {
        override fun execute(event: UseItemEvent) {
            println("You use ${event.source} on ${event.target}")
            val filteredUses = actions.filter { it.matches(event) }
            if (filteredUses.isEmpty()){
                println("Nothing happens.")
            } else {
                filteredUses.forEach { it.execute(event) }
            }
        }
    }
}