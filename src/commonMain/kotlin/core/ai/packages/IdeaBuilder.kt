package core.ai.packages

import core.events.Event
import core.thing.Thing
import use.interaction.nothing.NothingEvent

class IdeaBuilder(val name: String, val priority: Int, val takesTurn: Boolean) {
    internal var criteria: suspend (Thing) -> Boolean = { true }
    private var action: suspend (Thing) -> List<Event> = { listOf() }

    fun build(packageName: String) = Idea("$packageName-$name", priority, takesTurn, criteria, action)

    fun cond(condition: suspend (Thing) -> Boolean) {
        this.criteria = condition
    }

    fun actions(action: suspend (Thing) -> List<Event>) {
        this.action = action
    }

    fun act(action: suspend (Thing) -> Event?) {
        this.action = { listOfNotNull(action(it)) }
    }
}

class IdeasBuilder {
    private val children = mutableListOf<IdeaBuilder>()
    private val criteriaChildren = mutableMapOf<suspend (Thing) -> Boolean, IdeasBuilder>()

    fun getChildren(parentCriteria: suspend (Thing) -> Boolean = { true }): List<IdeaBuilder> {
        children.forEach { it.criteria = { thing -> parentCriteria(thing) && it.criteria(thing) } }

        return children + criteriaChildren.flatMap { (criteria, child) ->
            child.getChildren { thing -> parentCriteria(thing) && criteria(thing) }
        }
    }

    fun idea(item: IdeaBuilder) {
        children.add(item)
    }

    fun idea(name: String, priority: Int = 20, takesTurn: Boolean = true, initializer: IdeaBuilder.() -> Unit) {
        children.add(IdeaBuilder(name, priority, takesTurn).apply(initializer))
    }

    fun criteria(criteria: suspend (Thing) -> Boolean, initializer: IdeasBuilder.() -> Unit) {
        criteriaChildren[criteria] = IdeasBuilder().apply(initializer)
    }

}

fun ideas(initializer: IdeasBuilder.() -> Unit): List<IdeaBuilder> {
    return IdeasBuilder().apply(initializer).getChildren()
}
