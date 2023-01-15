package core.ai.desire

import core.thing.Thing

typealias PrioritizedAgendaName = Pair<String, Int>

data class DesireTree(
    private val condition: suspend (Thing) -> Boolean?,
    private val agendas: List<PrioritizedAgendaName> = listOf(),
    private val children: List<DesireTree> = listOf()
) {

    suspend fun getDesires(source: Thing): List<PrioritizedAgendaName> {
        return if (condition(source) == true) {
            agendas + children.flatMap { it.getDesires(source) }
        } else listOf()
    }

    fun getAllDesires(): List<PrioritizedAgendaName> {
        return agendas + children.flatMap { it.getAllDesires() }
    }
}