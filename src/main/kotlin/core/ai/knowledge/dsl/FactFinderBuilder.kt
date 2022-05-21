package core.ai.knowledge.dsl

import core.ai.knowledge.Fact
import core.ai.knowledge.FactFinder
import core.ai.knowledge.Mind
import core.ai.knowledge.Subject

class FactFinderBuilder(private val kind: String) {
    private var relevantSource: (Subject) -> Boolean = { true }
    private var findFact: ((mind: Mind, source: Subject, kind: String) -> Fact) = {_, source, kind -> Fact(source, kind, 0, 0) }

    fun build(): FactFinder {
        return FactFinder(kind, relevantSource, findFact)
    }

    fun relevant(relevantSource: (Subject) -> Boolean){
        this.relevantSource = relevantSource
    }
    fun relevant(name: String){
        this.relevantSource = { source -> source.name.equals(name, ignoreCase = true)}
    }

    fun find(findFact: ((mind: Mind, source: Subject, kind: String) -> Fact)){
        this.findFact = findFact
    }

}

fun fact(kind: String, initializer: FactFinderBuilder.() -> Unit): FactFinder {
    return FactFinderBuilder(kind).apply(initializer).build()
}