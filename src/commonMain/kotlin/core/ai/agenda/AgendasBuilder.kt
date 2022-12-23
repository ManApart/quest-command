package core.ai.agenda

import core.events.Event
import core.thing.Thing

class AgendasBuilder {
    internal val children = mutableListOf<AgendaBuilder>()

    fun agenda(item: AgendaBuilder) {
        children.add(item)
    }

    fun agenda(name: String, initializer: AgendaBuilder.() -> Unit) {
        children.add(AgendaBuilder(name).apply(initializer))
    }

    //Creates an Agenda with an action of the same name and a single result event
    fun agendaAction(name: String, result: (Thing) -> Event?) {
        agenda(name){
            action(name, result)
        }
    }

    fun build(): List<Agenda> {
        return children.map { it.build() }
    }
}

fun agendas(
    initializer: AgendasBuilder.() -> Unit
): List<Agenda> {
    return AgendasBuilder().apply(initializer).build()
}
