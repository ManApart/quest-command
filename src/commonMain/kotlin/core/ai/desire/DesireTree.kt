package core.ai.desire

import core.conditional.Context
import core.thing.Thing

typealias PrioritizedAgendaName = Pair<String, Int>

data class DesireTree(
    private val condition: (Thing, Context) -> Boolean?,
    private val agendas: List<PrioritizedAgendaName> = listOf(),
    private val context: Context = Context(),
    private val children: List<DesireTree> = listOf()
) {

    fun getDesires(source: Thing): List<PrioritizedAgendaName> {
        return if (condition(source, context) == true) {
            agendas + children.flatMap { it.getDesires(source) }
        } else listOf()
    }

    fun getAllDesires(): List<PrioritizedAgendaName> {
        return agendas + children.flatMap { it.getAllDesires() }
    }
}