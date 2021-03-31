package conversation.dsl

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.events.Event
import core.target.Target
import core.utility.Named
import traveling.location.location.LocationNode

class DialogueBuilder(val condition: (Conversation) -> Boolean) {
    var priority: Int? = null
    val depthScale: Int = 2
    private val children: MutableList<DialogueBuilder> = mutableListOf()
    private var results: MutableList<((Conversation) -> List<Event>)> = mutableListOf()

    fun cond(condition: (Conversation) -> Boolean = { true }, initializer: DialogueBuilder.() -> Unit) {
        children.add(conversations(condition, initializer))
    }

    fun resultLine(line: ((Conversation) -> String)) {
        this.results.add { listOf(DialogueEvent(it.getLatestListener(), it, line(it))) }
    }

    fun result(result: ((Conversation) -> Event)) {
        this.results.add { listOf(result(it)) }
    }

    fun results(results: ((Conversation) -> List<Event>)) {
        this.results.add(results)
    }

    fun build(): List<Dialogue> {
        return build(listOf())
    }

    private fun build(parentConditions: List<(Conversation) -> Boolean>, depth: Int = 0): List<Dialogue> {
        val conditions = parentConditions + listOf(condition)
        val evaluations = mutableListOf<Dialogue>()
        val usedPriority = priority ?: (10 + depthScale * depth)

        results.forEach { result ->
            evaluations.add(Dialogue(conditions, result, usedPriority))
        }

        if (children.isNotEmpty()) {
            evaluations.addAll(children.flatMap { it.build(conditions, depth + 1) })
        }

        return evaluations
    }
}

fun conversations(condition: (Conversation) -> Boolean = { true }, initializer: DialogueBuilder.() -> Unit): DialogueBuilder {
    return DialogueBuilder(condition).apply(initializer)
}

fun Conversation.question(): QuestionType? {
    return history.last().parsed?.questionType
}

fun Conversation.verb(): Verb? {
    return history.last().parsed?.verb
}

fun Conversation.subject(): Named? {
    return history.last().parsed?.subject
}

fun Conversation.subjects(): List<Named>? {
    return history.last().parsed?.subjects
}

fun Named?.hasTag(tag: String): Boolean {
    return if (this != null) {
        (this is LocationNode && getLocation().properties.tags.has(tag)) ||
                (this is Target && properties.tags.has(tag))
    } else {
        false
    }
}