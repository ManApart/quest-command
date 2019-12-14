package core.ai

import core.utility.NameSearchableList

class AIFakeParser(private val aiList: List<AIBase> = listOf()) : AIParser {
    override fun loadAI(): NameSearchableList<AIBase> = NameSearchableList(aiList)
}