package system.ai

import core.gameState.Target
import core.gameState.ai.AI
import core.gameState.ai.AIBase
import core.gameState.ai.PLAYER_CONTROLLED_ID
import core.utility.NameSearchableList
import system.DependencyInjector

object AIManager {
    private var AIs = loadAI()
    private val defaultAI = AIBase("NONE")
    private val playerControlledAI = AIBase(PLAYER_CONTROLLED_ID)

    fun reset() {
        AIs = loadAI()
    }

    private fun loadAI(): NameSearchableList<AIBase> {
        val parser = DependencyInjector.getImplementation(AIParser::class.java)
        return parser.loadAI()
    }

    fun getAI(name: String?, creature: Target): AI {
        return when {
            name == PLAYER_CONTROLLED_ID -> playerControlledAI.createPlayerControlled(creature)
            name != null && AIs.exists(name) -> AIs.get(name).createConditional(creature)
            else -> defaultAI.createConditional(creature)
        }
    }

}