package core.ai.packages

import core.thing.Thing

class AIPackageTemplateBuilder(val name: String) {
    private val templates = mutableListOf<String>()
    private val ideas = mutableListOf<IdeaBuilder>()
    private val criteriaChildren = mutableMapOf<suspend (Thing) -> Boolean, IdeasBuilder>()
    private val dropped = mutableListOf<String>()

    fun build(): AIPackageTemplate {
        val childIdeas = criteriaChildren.flatMap { (parentCriteria, builder) -> builder.getChildren(parentCriteria) }
        return AIPackageTemplate(name, (ideas + childIdeas).map { it.build() }, templates, dropped)
    }

    fun template(vararg names: String) = this.templates.addAll(names)

    fun drop(vararg ideaName: String) {
        dropped.addAll(ideaName)
    }

    fun idea(name: String, priority: Int = 20, takesTurn: Boolean = true, initializer: IdeaBuilder.() -> Unit = {}) {
        ideas.add(IdeaBuilder(name, priority, takesTurn).apply(initializer))
    }

    fun tagged(tag: String, initializer: IdeasBuilder.() -> Unit) {
        val cond: suspend (Thing) -> Boolean = { it.properties.tags.has(tag) }
        criteriaChildren[cond] = IdeasBuilder().apply(initializer)
    }

    fun cond(condition: suspend (Thing) -> Boolean, initializer: IdeasBuilder.() -> Unit) {
        criteriaChildren[condition] = IdeasBuilder().apply(initializer)
    }

}

//DO list of
fun aiPackage(name: String, initializer: AIPackageTemplateBuilder.() -> Unit): AIPackageTemplate {
    return AIPackageTemplateBuilder(name).apply(initializer).build()
}

class AIPackageTemplatesBuilder {
    internal val children = mutableListOf<AIPackageTemplateBuilder>()
    fun aiPackage(item: AIPackageTemplateBuilder) {
        children.add(item)
    }

    fun aiPackage(name: String, initializer: AIPackageTemplateBuilder.() -> Unit) {
        children.add(AIPackageTemplateBuilder(name).apply(initializer))
    }
}

fun aiPackages(initializer: AIPackageTemplatesBuilder.() -> Unit): List<AIPackageTemplate> {
    return AIPackageTemplatesBuilder().apply(initializer).children.map { it.build() }
}
