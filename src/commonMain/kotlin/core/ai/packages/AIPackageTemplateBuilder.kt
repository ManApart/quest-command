package core.ai.packages

import core.thing.Thing

//Build all the templates, but don't flatten them
//Using the dependency injection, we could have many things producing the templates
//Once all templates generated, validate / transform them into ai packages
//A mind references a single ai package by string
//Ideas _should_ only have a single instance, so can possibly be optimized


class AIPackageTemplateBuilder(val name: String) {
    private val templates = mutableListOf<String>()
    private val ideas = mutableListOf<IdeaBuilder>()
    private val criteriaChildren = mutableMapOf<suspend (Thing) -> Boolean, IdeasBuilder>()
    private val priorityOverride = mutableMapOf<String, Int>()
    private val dropped = mutableListOf<String>()

    fun build(): AIPackageTemplate {
        val childIdeas = criteriaChildren.flatMap { (parentCriteria, builder) -> builder.getChildren(parentCriteria) }
        return AIPackageTemplate(name, (ideas + childIdeas).map { it.build(name) }, templates, priorityOverride, dropped)
    }

    fun template(vararg names: String) = this.templates.addAll(names)

    /**
    Override the priority of an idea inherited from a template.
     */
    fun priority(ideaName: String, newPriority: Int){
        priorityOverride[ideaName] = newPriority
    }

    fun drop(vararg ideaName: String){
        dropped.addAll(ideaName)
    }

    fun idea(name: String, priority: Int = 20, initializer: IdeaBuilder.() -> Unit = {}) {
        ideas.add(IdeaBuilder(name, priority).apply(initializer))
    }

    fun criteria(criteria: suspend (Thing) -> Boolean, initializer: IdeasBuilder.() -> Unit){
        criteriaChildren[criteria] = IdeasBuilder().apply(initializer)
    }

}

//DO list of
fun aiPackage(name: String, initializer: AIPackageTemplateBuilder.() -> Unit): AIPackageTemplate {
    return AIPackageTemplateBuilder(name).apply(initializer).build()
}

class AIPackageTemplatesBuilder {
    internal val children = mutableListOf<AIPackageTemplateBuilder>()
    fun aiPackage(item: AIPackageTemplateBuilder){
        children.add(item)
    }

    fun aiPackage(name: String, initializer: AIPackageTemplateBuilder.() -> Unit){
        children.add(AIPackageTemplateBuilder(name).apply(initializer))
    }

}

fun aiPackages(initializer: AIPackageTemplatesBuilder.() -> Unit): List<AIPackageTemplate> {
    return AIPackageTemplatesBuilder().apply(initializer).children.map { it.build() }
}