package core.ai

import core.target.Target
import core.utility.Named
import quests.ConditionalEvents

class AIBase(override val name: String, val actions: List<ConditionalEvents<*>> = listOf()) : Named {

    fun createConditional(creature: Target) : AI {
        return ConditionalAI(name, creature, actions)
    }

    fun createPlayerControlled(creature: Target) : AI {
        return PlayerControlledAI(creature)
    }

}