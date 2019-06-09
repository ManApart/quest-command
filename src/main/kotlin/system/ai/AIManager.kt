package system.ai

import core.gameState.AI
import core.gameState.AIBase
import core.gameState.Target
import core.utility.NameSearchableList
import system.DependencyInjector

object AIManager {
    private var AIs = loadAI()

    fun reset() {
        AIs = loadAI()
    }

    private fun loadAI(): NameSearchableList<AIBase> {
        val parser = DependencyInjector.getImplementation(AIParser::class.java)
        return parser.loadAI()
    }

    fun getAI(name: String, creature: Target) : AI {
        return AIs.get(name).create(creature)
    }

}