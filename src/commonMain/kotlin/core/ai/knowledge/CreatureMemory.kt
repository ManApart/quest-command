package core.ai.knowledge

import core.utility.putAbsent

data class CreatureMemory(
    private val facts: MutableMap<String, MutableMap<SimpleSubject, Fact>> = mutableMapOf(),
    private val listFacts: MutableMap<String, ListFact> = mutableMapOf(),
) {
    constructor(facts: List<Fact>, listFacts: List<ListFact>) : this(facts.parsedFacts(), listFacts.parsedListFacts())

    fun getFact(source: Subject, kind: String) = getFact(SimpleSubject(source), kind)
    fun getFact(source: SimpleSubject, kind: String): Fact? {
        return facts[kind]?.get(source)
    }

    fun getAllFacts(): List<Fact> {
        return facts.values.flatMap { it.values }
    }

    fun getAllFacts(kind: String): List<Fact> {
        return facts[kind]?.values?.toList() ?: listOf()
    }

    fun getListFact(kind: String): ListFact? {
        return listFacts[kind]
    }

    fun getAllListFacts(): List<ListFact> {
        return listFacts.values.toList()
    }

    fun remember(fact: Fact) {
        facts.putAbsent(fact.kind, mutableMapOf())
        facts[fact.kind]?.put(fact.source, fact)
    }

    fun remember(fact: ListFact) {
        listFacts[fact.kind] = fact
    }

    fun forget(fact: Fact) {
        facts[fact.kind]?.remove(fact.source)
    }

    fun forget(fact: ListFact) {
        listFacts.remove(fact.kind)
    }

    fun forget() {
        facts.clear()
    }
}

private fun List<Fact>.parsedFacts(): MutableMap<String, MutableMap<SimpleSubject, Fact>> {
    val facts = mutableMapOf<String, MutableMap<SimpleSubject, Fact>>()
    forEach { fact ->
        facts.putAbsent(fact.kind, mutableMapOf())
        facts[fact.kind]?.put(fact.source, fact)
    }
    return facts
}

private fun List<ListFact>.parsedListFacts(): MutableMap<String, ListFact> {
    return groupBy { it.kind }.mapValues { it.value.sum() }.toMutableMap()
}