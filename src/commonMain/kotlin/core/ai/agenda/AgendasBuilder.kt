package core.ai.agenda

import core.conditional.Context
import core.thing.Thing

class AgendasBuilder {
    internal val children = mutableListOf<AgendaBuilder>()

    fun agenda(item: AgendaBuilder) {
        children.add(item)
    }

    fun agenda(name: String, initializer: AgendaBuilder.() -> Unit) {
        children.add(AgendaBuilder(name).apply(initializer))
    }

    fun build(): List<Agenda>{
        return children.map { it.build() }
    }
}

fun agendas(
    context: MutableMap<String, (Thing, Context) -> Any?> = mutableMapOf(),
    initializer: AgendasBuilder.() -> Unit
): List<Agenda> {
    return AgendasBuilder().apply(initializer).build()
}
