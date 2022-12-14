package core.ai.desire

import core.conditional.Context
import core.thing.Thing
import core.utility.Named

data class Desire(
    override val name: String,
    val priority: Int,
    val agenda: String
) : Named

data class DesireTree(
    private val condition: (Thing, Context) -> Boolean?,
    private val desires: List<Desire> = listOf(),
    private val context: Context = Context(),
    private val children: List<DesireTree> = listOf()
) {

    fun getDesires(source: Thing): List<Desire> {
        return if (condition(source, context) == true){
            desires + children.flatMap { it.getDesires(source) }
        } else listOf()
    }
}