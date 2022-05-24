package core.ai.knowledge

class ShortTermMemory {
    private val cachedFacts = mutableMapOf<String, MutableMap<Subject, Fact>>()

    fun getFact(source: Subject, kind: String): Fact? {
        return cachedFacts[kind]?.get(source)
    }

    fun remember(fact: Fact){
        cachedFacts.putIfAbsent(fact.kind, mutableMapOf())
        cachedFacts[fact.kind]?.put(fact.source, fact)
    }

    fun forget(){
        cachedFacts.clear()
    }
}