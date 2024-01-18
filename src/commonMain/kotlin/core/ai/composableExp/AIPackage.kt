package core.ai.composableExp

import core.events.Event
import core.thing.Thing

//How does priority work? Is it just based on order definition? Then what about sub modules
//If everything is order based, then all top level ideas happen before any children, and children are priority per order
//Could also provide priority override map of name to priority (string,Int)
//give a base priority, and then packages can override that priority, with the top package overriding the override of a child package
//When evaluating which idea to go with, map all ideas by priority, and grab the first idea that matches at a given priority list (or random at that level?)

//TODO - make name unique by prefixing it with parant ai package? How do we prevent name collisions?
data class Idea(val name: String, val criteria: suspend (Thing) -> Boolean, val action: suspend (Thing) -> List<Event>)

//TODO - we can validate string references before flattening
data class AIPackageTemplate(val name: String, val subPackages: List<String>, val ideas: List<Idea>) {
    fun flatten(reference: Map<String, AIPackageTemplate>, flattenedReference: MutableMap<String, AIPackage>): AIPackage {
        return flattenedReference[name] ?: let {
            val subIdeas = subPackages.mapNotNull { reference[it] }.flatMap { subPackage ->
                flattenedReference[subPackage.name]?.ideas
                    ?: subPackage.flatten(reference, flattenedReference).also { flattenedReference[subPackage.name] = it }.ideas
            }
            AIPackage(name, ideas + subIdeas)
        }
    }
}

data class AIPackage(val name: String, val ideas: List<Idea>)
