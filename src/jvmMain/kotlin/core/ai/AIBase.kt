package core.ai

import core.ai.action.AIAction
import core.utility.Named

class AIBase(override val name: String, private val actionNames: List<String> = listOf()) : Named {
    val actions: List<AIAction> by lazy { actionNames.map { AIManager.getAIAction(it) }}

    fun createConditional(): AI {
        return ConditionalAI(name, actions)
    }

    fun createPlayerControlled(): AI {
        return PlayerControlledAI()
    }

}