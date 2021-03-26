package conversation.input

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import core.events.Event

class DialogueBuilder {
//TODO - are conditions just using the default? Add more tests and make sure evaluates
    var condition: (Conversation) -> Boolean = { true }
    var priority: Int? = null
    private val children: MutableList<DialogueBuilder> = mutableListOf()
    private var results: ((Conversation) -> List<Event>)? = null

    fun convo(initializer: DialogueBuilder.() -> Unit) {
        children.add(conversations(initializer))
    }

    fun resultLine(line: ((Conversation) -> String)) {
        this.results = { it: Conversation -> listOf(DialogueEvent(it.getLatestListener(), it, line(it))) }
    }

    fun result(result: ((Conversation) -> Event)) {
        this.results = { listOf(result(it)) }
    }

    fun results(results: ((Conversation) -> List<Event>)) {
        this.results = results
    }

    fun build(): List<Dialogue> {
        return build(listOf())
    }

    private fun build(parentConditions: List<(Conversation) -> Boolean>, depth: Int = 0): List<Dialogue> {
        val conditions = parentConditions + listOf(condition)
        val evaluations = mutableListOf<Dialogue>()
        val usedPriority = priority ?: (10 + 2 * depth)

        if (results != null) {
            evaluations.add(Dialogue(results!!, conditions, usedPriority))
        }

        if (children.isNotEmpty()) {
            evaluations.addAll(children.flatMap { it.build(conditions, depth + 1) })
        }

        return evaluations
    }
}

fun conversations(initializer: DialogueBuilder.() -> Unit): DialogueBuilder {
    return DialogueBuilder().apply(initializer)
}