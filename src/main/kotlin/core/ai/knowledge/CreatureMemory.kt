package core.ai.knowledge

data class CreatureMemory(
    private val facts: MutableMap<String, MutableMap<SimpleSubject, Fact>> = mutableMapOf(),
    private val relationships: MutableMap<SimpleSubject, MutableMap<String, MutableMap<SimpleSubject, Relationship>>> = mutableMapOf()
) {
    constructor(facts: List<Fact>, relationships: List<Relationship>) : this(facts.parsedFacts(), relationships.parsed())

    //Average all that match?
    fun getFact(source: Subject, kind: String) = getFact(SimpleSubject(source), kind)
    fun getFact(source: SimpleSubject, kind: String): Fact? {
        return facts[kind]?.get(source)
    }

    fun getAllFacts(): List<Fact> {
        return facts.values.flatMap { it.values }
    }

    fun getRelationship(source: Subject, kind: String, relatesTo: Subject) = getRelationship(SimpleSubject(source), kind, SimpleSubject(relatesTo))
    fun getRelationship(source: SimpleSubject, kind: String, relatesTo: SimpleSubject): Relationship? {
        return relationships[source]?.get(kind)?.get(relatesTo)
    }

    fun getAllRelationships(): List<Relationship> {
        return relationships.values.flatMap { it.values }.flatMap { it.values }
    }

    fun remember(fact: Fact) {
        facts.putIfAbsent(fact.kind, mutableMapOf())
        facts[fact.kind]?.put(fact.source, fact)
    }

    fun remember(relationship: Relationship) {
        relationships.putIfAbsent(relationship.source, mutableMapOf())
        relationships[relationship.source]?.putIfAbsent(relationship.kind, mutableMapOf())
        relationships[relationship.source]?.get(relationship.kind)?.put(relationship.relatesTo, relationship)
    }

    fun forget() {
        facts.clear()
        relationships.clear()
    }
}

private fun List<Fact>.parsedFacts(): MutableMap<String, MutableMap<SimpleSubject, Fact>> {
    val facts = mutableMapOf<String, MutableMap<SimpleSubject, Fact>>()
    forEach { fact ->
        facts.putIfAbsent(fact.kind, mutableMapOf())
        facts[fact.kind]?.put(fact.source, fact)
    }
    return facts
}

private fun List<Relationship>.parsed(): MutableMap<SimpleSubject, MutableMap<String, MutableMap<SimpleSubject, Relationship>>> {
    val relationships = mutableMapOf<SimpleSubject, MutableMap<String, MutableMap<SimpleSubject, Relationship>>>()
    forEach { relationship ->
        relationships.putIfAbsent(relationship.source, mutableMapOf())
        relationships[relationship.source]?.putIfAbsent(relationship.kind, mutableMapOf())
        relationships[relationship.source]?.get(relationship.kind)?.put(relationship.relatesTo, relationship)
    }
    return relationships
}