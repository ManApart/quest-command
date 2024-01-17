package core.ai.composableExp


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