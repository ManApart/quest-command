package core.ai.composableExp

import core.events.Event
import core.thing.Thing

//When evaluating which idea to go with, map all ideas by priority, and grab the first idea that matches at a given priority list (or random at that level?)

data class Idea(val name: String, val priority: Int, val criteria: suspend (Thing) -> Boolean, val action: suspend (Thing) -> List<Event>)

//TODO - we can validate string references before flattening
//Validate
// - package name unique
// - idea name unique
// - string references exist
data class AIPackageTemplate(val name: String, val subPackages: List<String>, val priorityOverride: Map<String, Int>, val ideas: List<Idea>) {
    fun flatten(reference: Map<String, AIPackageTemplate>, flattenedReference: MutableMap<String, AIPackage>): AIPackage {
        return flattenedReference[name] ?: let {
            val subIdeas = subPackages.mapNotNull { reference[it] }.flatMap { subPackage ->
                flattenedReference[subPackage.name]?.ideas
                    ?: subPackage.flatten(reference, flattenedReference).also { flattenedReference[subPackage.name] = it }.ideas
            }.map { idea ->
                priorityOverride[it.name]?.let { idea.copy(priority = it) } ?: idea
            }
            AIPackage(name, ideas + subIdeas)
        }
    }
}

data class AIPackage(val name: String, val ideas: List<Idea>)
