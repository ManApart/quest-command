package core.ai.composableExp

import core.thing.Thing

//Can we memoize criteria per call to pickIdea? Would that be premature optimization?

data class AIPackage(val name: String, val ideas: Map<Int,List<Idea>>) {
    constructor(name: String, ideas: List<Idea>) : this(name, ideas.groupBy { it.priority })

    suspend fun pickIdea(source: Thing): Idea {
        return ideas.entries.sortedByDescending { it.key }.firstNotNullOfOrNull { (_, ideas) ->
            ideas.firstOrNull { it.criteria(source) }
        } ?: DO_NOTHING_IDEA
    }
}
