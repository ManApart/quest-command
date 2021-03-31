package core.ai.dsl

import core.ai.AIBase

class AISetBuilder {
    private val aiBuilders = mutableMapOf<String, AIBaseBuilder>()

    fun ai(name: String, initializer: AIBaseBuilder.() -> Unit) {
        aiBuilders[name] = AIBaseBuilder(name).apply(initializer)
    }

    fun build(): List<AIBase> {
        aiBuilders.values.forEach { it.prep(aiBuilders) }
        return aiBuilders.values.map { it.build() }
    }
}

fun aiBuilder(initializer: AISetBuilder.() -> Unit): AISetBuilder {
    return AISetBuilder().apply(initializer)
}