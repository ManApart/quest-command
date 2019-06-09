package core.gameState

import core.utility.Named

class AIBase(override val name: String) : Named {

    fun create(creature: Target) : AI {
        return AI(name, creature)
    }


}