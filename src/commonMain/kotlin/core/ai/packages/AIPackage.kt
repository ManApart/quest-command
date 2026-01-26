package core.ai.packages

import core.GameState
import core.GameStateKeys.DEBUG_PACKAGE
import core.thing.Thing
import system.debug.DebugType

//Can we memoize criteria per call to pickIdea? Would that be premature optimization?

data class AIPackage(val name: String, val ideas: Map<Int, List<Idea>>) {
    constructor(name: String, ideas: List<Idea>) : this(name, ideas.groupBy { it.priority })

    suspend fun pickIdea(source: Thing): Idea {
        val verbose = GameState.getDebugBoolean(DebugType.VERBOSE_AI) && GameState.properties.values[DEBUG_PACKAGE] == name
        if (verbose) debugWhyPicked(source)
        return (ideas.entries.sortedByDescending { it.key }.firstNotNullOfOrNull { (_, ideas) ->
            ideas.firstOrNull { it.criteria(source) }
        } ?: DO_NOTHING_IDEA)
            .also { if (verbose) println("\tPicked ${it.name}")}
    }

    private suspend fun debugWhyPicked(source: Thing) {
        println("AI Package $name for ${source.name}")
        ideas.entries
            .flatMap { (p, ideas) -> ideas.map { p to it } }
            .filter { (_, idea) -> idea.criteria(source) }
            .forEach { (priority, idea) ->
                println("\t$priority ${idea.name}")
            }
    }
}
