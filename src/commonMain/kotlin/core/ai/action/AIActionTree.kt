package core.ai.action

import core.conditional.Context
import core.thing.Thing

//TODO - write tests for this plus builder
data class AIActionTree(
    private val condition: (Thing, Context) -> Boolean?,
    private val actions: List<AIAction> = listOf(),
    private val context: Context = Context(),
    private val children: List<AIActionTree> = listOf()
) {

    fun getActions(source: Thing): List<AIAction> {
        return if (condition(source, context) == true){
            actions + children.flatMap { it.getActions(source) }
        } else listOf()
    }
}
