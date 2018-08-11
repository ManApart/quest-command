package core.gameState

class AIBase(val name: String) {

    fun create(creature: Creature) : AI {
        return AI(name, creature)
    }


}