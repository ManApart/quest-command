package core.ai

import core.ai.AIBase
import core.utility.NameSearchableList

interface AIParser {
    fun loadAI() : NameSearchableList<AIBase>
}