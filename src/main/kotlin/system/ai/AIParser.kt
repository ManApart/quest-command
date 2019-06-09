package system.ai

import core.gameState.AIBase
import core.utility.NameSearchableList

interface AIParser {
    fun loadAI() : NameSearchableList<AIBase>
}