package conversation.input

import conversation.dialogue.ConversationContext

class EvaluationBuilder {
    var condition: (ConversationContext) -> Boolean = { true }
    val children: MutableList<EvaluationBuilder> = mutableListOf()
    var result: ((ConversationContext) -> String)? = null

    fun convo(condition: (ConversationContext) -> Boolean = { true }, initializer: EvaluationBuilder.() -> Unit)  {
        children.add(conversation.input.convo(condition, initializer))
    }

    fun build(): List<Evaluation> {
        return build(listOf())
    }

    private fun build(parentConditions: List<(ConversationContext) -> Boolean>): List<Evaluation> {
        val conditions = parentConditions + listOf(condition)
        val results = mutableListOf<Evaluation>()

        if (result != null) {
            results.add(Evaluation(result!!, conditions))
        }

        if (children.isNotEmpty()) {
            results.addAll(children.flatMap { it.build(conditions) })
        }

        return results
    }
}

fun convo(condition: (ConversationContext) -> Boolean = { true }, initializer: EvaluationBuilder.() -> Unit): EvaluationBuilder {
    return EvaluationBuilder().apply(initializer)
}