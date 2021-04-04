package core.ai.dsl

import core.ai.AIBase
import java.lang.IllegalArgumentException

class AIBaseBuilder(val name: String) {
    var inherits: List<String> = listOf()
    var additional: List<String> = listOf()
    var exclude: List<String> = listOf()
    private val completeActionList = mutableSetOf<String>()
    private var prepped = false

    fun build(): AIBase {
        return AIBase(name, completeActionList.toList())
    }

    fun prep(aiBuilders: Map<String, AIBaseBuilder>, dependents: MutableSet<AIBaseBuilder> = mutableSetOf()) {
        if (dependents.contains(this)) {
            throw IllegalArgumentException("Circular Dependency found with $name in list ${dependents.joinToString(",") { it.name }}")
        }
        if (!prepped) {
            inheritFromParent(aiBuilders, dependents)
            completeActionList.addAll(additional)
            completeActionList.removeAll(exclude)
            prepped = true
        }
    }

    private fun inheritFromParent(aiBuilders: Map<String, AIBaseBuilder>, dependents: MutableSet<AIBaseBuilder>) {
        inherits.forEach { parentName ->
            val parent = aiBuilders[parentName]
            if (parent != null) {
                dependents.add(this)
                parent.prep(aiBuilders, dependents)
                completeActionList.addAll(parent.completeActionList)
            }
        }

    }
}

fun ai(name: String, initializer: AIBaseBuilder.() -> Unit): AIBaseBuilder {
    return AIBaseBuilder(name).apply(initializer)
}