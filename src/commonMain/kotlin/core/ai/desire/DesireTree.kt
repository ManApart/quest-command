package core.ai.desire

import core.conditional.Context
import core.thing.Thing

data class DesireTree(
    private val condition: (Thing, Context) -> Boolean?,
    private val agendas: List<Pair<String, Int>> = listOf(),
    private val context: Context = Context(),
    private val children: List<DesireTree> = listOf()
) {

    fun getDesires(source: Thing): List<Pair<String, Int>> {
        return if (condition(source, context) == true) {
            agendas + children.flatMap { it.getDesires(source) }
        } else listOf()
    }

    fun getAllDesires(): List<Pair<String, Int>> {
        return agendas + children.flatMap { it.getAllDesires() }
    }
}