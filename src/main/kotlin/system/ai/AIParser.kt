package system.ai

import core.gameState.ai.AIBase
import core.utility.NameSearchableList

interface AIParser {
    fun loadAI() : NameSearchableList<AIBase>
}