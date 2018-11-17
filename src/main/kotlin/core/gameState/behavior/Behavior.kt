package core.gameState.behavior

import core.events.Event
import core.gameState.Target

class Behavior(base: BehaviorBase, paramValues: Map<String, String>){
    val name = base.name
    private val condition = base.condition.applyParamValues(paramValues)
    private val events = base.events.asSequence().map { it.applyParamValues(paramValues) }.toList()


    fun evaluateAndExecute(target: Target, event: Event){
        if (condition.matches(event)){
            this.events.forEach {
                it.execute(target)
            }
        }
    }
}