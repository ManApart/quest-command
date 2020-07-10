package core.ai

import core.target.Target
import quests.triggerCondition.TriggeredEvent
import core.utility.Named

class AIBase(override val name: String, val actions: List<TriggeredEvent> = listOf()) : Named {

    fun createConditional(creature: Target) : AI {
        return ConditionalAI(name, creature, actions)
    }

    fun createPlayerControlled(creature: Target) : AI {
        return PlayerControlledAI(creature)
    }

}