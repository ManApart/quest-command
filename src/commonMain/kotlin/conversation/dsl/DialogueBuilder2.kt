package conversation.dsl

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import core.events.Event

class DialogueBuilder2(val condition: (Conversation) -> Boolean) {
    var priority: Int? = null
    private val depthScale: Int = 2
    private val children: MutableList<DialogueBuilder2> = mutableListOf()
    private var results: MutableList<((Conversation) -> List<Event>)> = mutableListOf()

    fun cond(condition: (Conversation) -> Boolean = { true }, initializer: DialogueBuilder2.() -> Unit) {
        children.add(DialogueBuilder2(condition).apply(initializer))
    }

    fun line(line: ((Conversation) -> String)) {
        this.results.add { listOf(DialogueEvent(it.getLatestListener(), it, line(it))) }
    }

    fun event(result: ((Conversation) -> Event)) {
        this.results.add { listOf(result(it)) }
    }

    fun events(results: ((Conversation) -> List<Event>)) {
        this.results.add(results)
    }

    internal fun build(depth: Int = 0): DialogueTree {
        val usedPriority = priority ?: (10 + depthScale * depth)

        val dialogues = results.map { Dialogue2(it, usedPriority) }
        val trees = children.map { it.build(depth + 1) }

        return DialogueTree(condition, dialogues, trees)
    }
}

fun conversations2(
    condition: (Conversation) -> Boolean = { true },
    initializer: DialogueBuilder2.() -> Unit
): List<DialogueTree> {
    return listOf(DialogueBuilder2(condition).apply(initializer).build())
}