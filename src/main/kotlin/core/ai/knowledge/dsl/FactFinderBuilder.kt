package core.ai.knowledge.dsl

import core.ai.knowledge.Fact
import core.ai.knowledge.FactFinder
import core.ai.knowledge.Mind
import core.ai.knowledge.Subject

class FactFinderBuilder {
    private var relevantKind: (String) -> Boolean = { true }
    private var relevantSource: MutableList<(Subject) -> Boolean> = mutableListOf({ true })
    private var findFact: ((mind: Mind, source: Subject, kind: String) -> Fact) = { _, source, kind -> Fact(source, kind, 0, 0) }

    fun build(): FactFinder {
        return FactFinder(relevantKind, relevantSource, findFact)
    }

    fun kind(kind: (String) -> Boolean) {
        this.relevantKind = kind
    }

    fun kind(name: String) {
        this.relevantKind = { kind -> name.equals(kind, ignoreCase = true) }
    }

    fun source(relevantSource: (Subject) -> Boolean) {
        this.relevantSource.add(relevantSource)
    }

    fun source(name: String) {
        this.relevantSource.add { source -> source.name.equals(name, ignoreCase = true) }
    }

    fun find(findFact: ((mind: Mind, source: Subject, kind: String) -> Fact)) {
        this.findFact = findFact
    }

}

fun fact(initializer: FactFinderBuilder.() -> Unit): FactFinder {
    return FactFinderBuilder().apply(initializer).build()
}