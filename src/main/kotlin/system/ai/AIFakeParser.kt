package system.ai

import core.gameState.ai.AIBase
import core.utility.NameSearchableList

class AIFakeParser(private val aiList: List<AIBase> = listOf()) : AIParser {
    override fun loadAI(): NameSearchableList<AIBase> = NameSearchableList(aiList)
}