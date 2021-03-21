package conversation.input

import conversation.Conversation
import core.events.Event

class DialogueBuilder {

    //speaker: Target, listener: Target, conversationHistory
    var condition: (Conversation) -> Boolean = { true }
    val children: MutableList<DialogueBuilder> = mutableListOf()
    var result: ((Conversation) -> Event)? = null
    var results: ((Conversation) -> List<Event>)? = null

    fun convo(condition: (Conversation) -> Boolean = { true }, initializer: DialogueBuilder.() -> Unit) {
        children.add(conversation.input.convo(condition, initializer))
    }

    fun build(): List<Dialogue> {
        return build(listOf())
    }

    private fun build(parentConditions: List<(Conversation) -> Boolean>): List<Dialogue> {
        val conditions = parentConditions + listOf(condition)
        val evaluations = mutableListOf<Dialogue>()

        if (result != null) {
            evaluations.add(Dialogue({ listOf(result!!(it)) }, conditions))
        }

        if (results != null) {
            evaluations.add(Dialogue(results!!, conditions))
        }

        if (children.isNotEmpty()) {
            evaluations.addAll(children.flatMap { it.build(conditions) })
        }

        return evaluations
    }
}

fun convo(condition: (Conversation) -> Boolean = { true }, initializer: DialogueBuilder.() -> Unit): DialogueBuilder {
    return DialogueBuilder().apply(initializer)
}