package core.ai.knowledge

class ShortTermMemory {
    private val cachedFacts = mutableMapOf<String, MutableMap<SubjectFilter, Fact>>()
    private val cachedRelationships = mutableMapOf<String, MutableMap<SubjectFilter, MutableMap<SubjectFilter, Relationship>>>()

    //Average all that match?
    fun getFact(source: SubjectFilter, kind: String): Fact? {
        return cachedFacts[kind]?.get(source)
    }

    fun getRelationship(source: SubjectFilter, kind: String, relatesTo: SubjectFilter): Relationship? {
        return cachedRelationships[kind]?.get(source)?.get(relatesTo)
    }

    fun remember(fact: Fact){
        cachedFacts.putIfAbsent(fact.kind, mutableMapOf())
        cachedFacts[fact.kind]?.put(fact.source, fact)
    }

    fun remember(relationship: Relationship){
        cachedRelationships.putIfAbsent(relationship.kind, mutableMapOf())
        cachedRelationships[relationship.kind]?.putIfAbsent(relationship.source, mutableMapOf())
        cachedRelationships[relationship.kind]?.get(relationship.source)?.put(relationship.source, relationship)
    }

    fun forget(){
        cachedFacts.clear()
        cachedRelationships.clear()
    }
}