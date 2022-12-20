package conversation.dsl

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.events.Event
import core.utility.Named

class DialogueBuilder2 {
    var priority: Int? = null
    private val depthScale: Int = 2
    private val conditions: MutableList<(Conversation) -> Boolean> = mutableListOf()
    private val children: MutableList<DialogueBuilder2> = mutableListOf()
    private var results: MutableList<((Conversation) -> List<Event>)> = mutableListOf()

    fun cond(condition: (Conversation) -> Boolean) {
        conditions.add(condition)
    }

    fun question(questionType: QuestionType) {
        cond { it.question() == questionType }
    }

    fun verb(verb: Verb) {
        cond { it.verb() == verb }
    }

    fun subject(condition: (Conversation) -> Named) {
        cond { it.subject() == condition(it) }
    }

    fun child(initializer: DialogueBuilder2.() -> Unit) {
        children.add(DialogueBuilder2().apply(initializer))
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
        val condition: (Conversation) -> Boolean = { convo -> conditions.all { it(convo) } }
        val dialogues = results.map { Dialogue2(it, usedPriority) }
        val trees = children.map { it.build(depth + 1) }

        return DialogueTree(condition, dialogues, trees)
    }
}

fun conversations2(
    initializer: DialogueBuilder2.() -> Unit
): DialogueTree {
    return DialogueBuilder2().apply(initializer).build()
}