package core.gameState.ai

import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import core.utility.Named

class AIBase(override val name: String, val actions: List<TriggeredEvent> = listOf()) : Named {

    fun createConditional(creature: Target) : AI {
        return ConditionalAI(name, creature, actions)
    }

    fun createPlayerControlled(creature: Target) : AI {
        return PlayerControlledAI(creature)
    }


}