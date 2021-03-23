package conversation.input

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import core.events.Event

class DialogueBuilder {

    //speaker: Target, listener: Target, conversationHistory
    var condition: (Conversation) -> Boolean = { true }
    val children: MutableList<DialogueBuilder> = mutableListOf()
    var result: ((Conversation) -> Event)? = null
    var resultLine: ((Conversation) -> String)? = null
    var results: ((Conversation) -> List<Event>)? = null
    var priority: Int? = null

    fun convo(condition: (Conversation) -> Boolean = { true }, initializer: DialogueBuilder.() -> Unit) {
        children.add(conversation.input.convo(condition, initializer))
    }

    fun build(): List<Dialogue> {
        return build(listOf())
    }

    private fun build(parentConditions: List<(Conversation) -> Boolean>, depth: Int = 0): List<Dialogue> {
        val conditions = parentConditions + listOf(condition)
        val evaluations = mutableListOf<Dialogue>()
        val usedPriority = priority ?: (10 + 2 * depth)

        if (result != null) {
            evaluations.add(Dialogue({ listOf(result!!(it)) }, conditions, usedPriority))
        }

        if (resultLine != null) {
            val res = { it: Conversation -> listOf(DialogueEvent(it.getLatestListener(), it, resultLine!!(it))) }
            evaluations.add(Dialogue(res, conditions, usedPriority))
        }

        if (results != null) {
            evaluations.add(Dialogue(results!!, conditions, usedPriority))
        }

        if (children.isNotEmpty()) {
            evaluations.addAll(children.flatMap { it.build(conditions, depth + 1) })
        }

        return evaluations
    }
}

fun convo(condition: (Conversation) -> Boolean = { true }, initializer: DialogueBuilder.() -> Unit): DialogueBuilder {
    return DialogueBuilder().apply(initializer)
}