package core.gameState

class AIBase(val name: String) {

    fun create(creature: Target) : AI {
        return AI(name, creature)
    }


}