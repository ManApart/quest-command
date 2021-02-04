package conversation.input

class EvaluationBuilder(val condition: () -> Boolean, var result: String? = null, val children: MutableList<EvaluationBuilder> = mutableListOf()) {
}