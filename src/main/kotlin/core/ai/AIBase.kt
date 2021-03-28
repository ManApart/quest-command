package core.ai

import core.ai.action.AIAction
import core.target.Target
import core.utility.Named

class AIBase(override val name: String, val actions: List<AIAction> = listOf()) : Named {

    fun createConditional(creature: Target) : AI {
        return ConditionalAI(name, creature, actions)
    }

    fun createPlayerControlled(creature: Target) : AI {
        return PlayerControlledAI(creature)
    }

}