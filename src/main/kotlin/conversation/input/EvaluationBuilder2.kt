package conversation.input

class EvaluationBuilder2(private val condition: () -> Boolean = { true }, private val children: List<EvaluationBuilder2> = mutableListOf(), private val result: String? = null) {

    fun build(): List<Evaluation> {
        return build(listOf())
    }

    private fun build(parentConditions: List<() -> Boolean>): List<Evaluation> {
        val conditions = parentConditions + listOf(condition)
        val results = mutableListOf<Evaluation>()

        if (result != null) {
            results.add(Evaluation(result, conditions))
        }

        if (children.isNotEmpty()) {
            results.addAll(children.flatMap { it.build(conditions) })
        }

        return results
    }
}