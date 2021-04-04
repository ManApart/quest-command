package core.ai

import core.ai.action.AIAction
import core.target.Target
import core.utility.Named

class AIBase(override val name: String, private val actionNames: List<String> = listOf()) : Named {
    val actions: List<AIAction> by lazy { actionNames.map { AIManager.getAIAction(it) }}

    fun createConditional(creature: Target) : AI {
        return ConditionalAI(name, creature, actions)
    }

    fun createPlayerControlled(creature: Target) : AI {
        return PlayerControlledAI(creature)
    }

}