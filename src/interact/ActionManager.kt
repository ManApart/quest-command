package interact

import core.events.EventListener
import interact.actions.Action
import core.utility.ReflectionTools

object ActionManager {
    private val actions = loadActions()

    private fun loadActions(): List<Action> {
        return ReflectionTools.getAllUses().map { it.newInstance() }.toList()
    }

    class UseHandler() : EventListener<UseEvent>() {
        override fun execute(event: UseEvent) {
            val filteredUses = actions.filter { it.matches(event) }
            if (filteredUses.isEmpty()){
                println("You use ${event.source} on ${event.target} but nothing happens.")
            } else {
                filteredUses.forEach { it.execute(event) }
            }
        }
    }
}