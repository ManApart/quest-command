package core.ai.composableExp

import core.events.Event
import core.thing.Thing

//Build all the templates, but don't flatten them
//Using the dependency injection, we could have many things producing the templates
//Once all templates generated, validate / transform them into ai packages
//A mind references a single ai package by string
//Ideas _should_ only have a single instance, so can possibly be optimized


class AIPackageTemplateBuilder(val name: String) {
    private val templates = mutableListOf<String>()
    private val ideas = mutableListOf<Idea>()


    fun build(): AIPackageTemplate {
        return AIPackageTemplate(name, templates, ideas)
    }

    fun template(vararg names: String) = this.templates.addAll(names)

    fun idea(name: String, initializer: IdeaBuilder.() -> Unit = {}) {
        ideas.add(IdeaBuilder(name).apply(initializer).build())
    }

}

//DO list of
fun aiPackage(name: String, initializer: AIPackageTemplateBuilder.() -> Unit): AIPackageTemplate {
    return AIPackageTemplateBuilder(name).apply(initializer).build()
}